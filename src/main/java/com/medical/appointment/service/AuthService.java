package com.medical.appointment.service;

import com.medical.appointment.dto.admin.request.CreateAdminRequest;
import com.medical.appointment.dto.admin.response.AdminResponse;
import com.medical.appointment.dto.auth.request.*;
import com.medical.appointment.dto.auth.response.AuthResponse;
import com.medical.appointment.dto.doctor.request.DoctorRegisterRequest;
import com.medical.appointment.dto.doctor.response.DoctorResponse;
import com.medical.appointment.dto.patient.request.PatientRegisterRequest;
import com.medical.appointment.dto.patient.response.PatientResponse;
import com.medical.appointment.dto.staff.request.StaffRegisterRequest;
import com.medical.appointment.dto.staff.response.StaffResponse;
import com.medical.appointment.dto.user.response.UserResponse;
import com.medical.appointment.exception.*;
import com.medical.appointment.model.*;
import com.medical.appointment.model.enums.AccessLevel;
import com.medical.appointment.model.enums.UserRole;
import com.medical.appointment.repository.*;
import com.medical.appointment.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.medical.appointment.dto.user.response.UserSummaryResponse;
import com.medical.appointment.dto.auth.request.ForgotPasswordRequest;
import com.medical.appointment.dto.auth.request.ResetPasswordRequest;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;

import static com.medical.appointment.util.PasswordUtils.generateRandomPassword;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final StaffRepository staffRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final EmailService emailService;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int VERIFICATION_CODE_EXPIRY_MINUTES = 5;
    private static final int PASSWORD_RESET_CODE_EXPIRY_MINUTES = 10;

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        if (!user.getIsActive()) {
            throw new AccessDeniedException("Account is deactivated. Please contact support.");
        }

        String roleClaim = UserRole.fromInt(user.getRoleType()).name();
        String accessLevel = null;

        if (user.getRoleType() == UserRole.ADMIN.getValue()) {
            Admin admin = adminRepository.findById(user.getUserId())
                    .orElseThrow(() -> new AccessDeniedException("Admin profile missing"));

            roleClaim = UserRole.ADMIN.name();
            accessLevel = admin.getAccessLevel().name();
        }

        String token = jwtTokenUtil.generateToken(user.getEmail(), roleClaim, accessLevel);

        UserSummaryResponse userSummary = new UserSummaryResponse(
                user.getUserId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRoleType(),
                UserRole.fromInt(user.getRoleType()).name(),
                accessLevel,
                user.getIsActive()
        );

        return new AuthResponse(token, userSummary);
    }

    @Transactional
    public PatientResponse registerPatient(PatientRegisterRequest request) {
        validateUniqueness(request.getEmail(), request.getNic());

        User user = prepareNewUser(request, UserRole.PATIENT);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        Patient patient = new Patient();
        patient.setUser(savedUser);
        patient.setEmergencyContact(blankToNull(request.getEmergencyContact()));
        patient.setBloodGroup(request.getBloodGroup());
        patient.setAllergies(blankToNull(request.getAllergies()));

        Patient savedPatient = patientRepository.save(patient);

        emailService.sendVerificationEmail(
                savedUser.getEmail(),
                savedUser.getVerificationCode()
        );

        return mapToPatientResponse(savedPatient);
    }

    @Transactional
    public DoctorResponse registerDoctor(DoctorRegisterRequest request) {
        getAuthenticatedAdmin();
        validateUniqueness(request.getEmail(), request.getNic());

        User user = prepareNewUser(request, UserRole.DOCTOR);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);

        emailService.sendVerificationEmail(savedUser.getEmail(), savedUser.getVerificationCode());

        Doctor doctor = new Doctor();
        doctor.setUser(savedUser);
        doctor.setSpecialization(request.getSpecialization());
        doctor.setLicenseNumber(request.getLicenseNumber());
        doctor.setQualification(request.getQualification());
        doctor.setExperienceYears(request.getExperienceYears());
        doctor.setConsultationFee(request.getConsultationFee());
        Doctor savedDoctor = doctorRepository.save(doctor);

        return mapToDoctorResponse(savedDoctor);
    }

    private String blankToNull(String value) {
        return value == null || value.trim().isEmpty() ? null : value.trim();
    }

    @Transactional
    public StaffResponse registerStaff(StaffRegisterRequest request) {
        getAuthenticatedAdmin();
        validateUniqueness(request.getEmail(), request.getNic());

        User user = prepareNewUser(request, UserRole.STAFF);
        user.setRoleType(UserRole.STAFF.getValue());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);

        emailService.sendVerificationEmail(savedUser.getEmail(), savedUser.getVerificationCode());

        Staff staff = new Staff();
        staff.setUser(savedUser);
        staff.setStatus(request.getStatus());
        staff.setWorkingHours(request.getWorkingHours());
        staff.setSpecialization(request.getSpecialization());
        Staff savedStaff = staffRepository.save(staff);

        return mapToStaffResponse(savedStaff);
    }

    @Transactional
    public AdminResponse registerAdmin(CreateAdminRequest request) {
        Admin loggedInAdmin = getAuthenticatedAdmin();
        if (loggedInAdmin.getAccessLevel() != AccessLevel.SUPER_ADMIN) {
            throw new AccessDeniedException("Only Super Admins can create new Admin accounts.");
        }

        validateUniqueness(request.getEmail(), request.getNic());

        User user = prepareNewUser(request, UserRole.ADMIN);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);

        emailService.sendVerificationEmail(savedUser.getEmail(), savedUser.getVerificationCode());

        Admin admin = new Admin();
        admin.setUser(savedUser);
        admin.setDepartment(request.getDepartment());
        admin.setAccessLevel(request.getAccessLevel());
        Admin savedAdmin = adminRepository.save(admin);

        return mapToAdminResponse(savedAdmin);
    }

    @Transactional
    public void verifyAccount(VerificationRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));

        if (user.getIsActive()) {
            throw new IllegalStateException("Account is already verified.");
        }

        if (user.getCodeExpiry() == null || user.getCodeExpiry().isBefore(LocalDateTime.now())) {
            throw new VerificationException("Verification code has expired. Please request a new verification code.");
        }

        if (!user.getVerificationCode().equals(request.getCode())) {
            throw new InvalidCredentialsException("Invalid verification code.");
        }

        user.setIsActive(true);
        user.setVerificationCode(null);
        user.setCodeExpiry(null);

        userRepository.save(user);
    }

    @Transactional
    public void resendVerificationCode(ResendVerificationRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));

        if (user.getIsActive()) {
            throw new IllegalStateException("Account is already verified.");
        }

        user.setVerificationCode(generateVerificationCode());
        user.setCodeExpiry(LocalDateTime.now().plusMinutes(VERIFICATION_CODE_EXPIRY_MINUTES));

        userRepository.save(user);

        emailService.sendVerificationEmail(user.getEmail(), user.getVerificationCode());
    }

    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        /*
         * Do not reveal whether the email exists or not.
         * This helps prevent email enumeration attacks.
         */
        if (user == null || !user.getIsActive()) {
            return;
        }

        user.setPasswordResetCode(generateVerificationCode());
        user.setPasswordResetExpiry(
                LocalDateTime.now().plusMinutes(PASSWORD_RESET_CODE_EXPIRY_MINUTES)
        );

        userRepository.save(user);
        emailService.sendPasswordResetEmail(user.getEmail(), user.getPasswordResetCode());
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid reset request."));

        if (!user.getIsActive()) {
            throw new AccessDeniedException("Account is not active. Please verify your account first.");
        }

        if (user.getPasswordResetCode() == null || user.getPasswordResetExpiry() == null) {
            throw new InvalidCredentialsException("Invalid or expired reset code.");
        }

        if (user.getPasswordResetExpiry().isBefore(LocalDateTime.now())) {
            user.setPasswordResetCode(null);
            user.setPasswordResetExpiry(null);
            userRepository.save(user);

            throw new VerificationException("Password reset code has expired. Please request a new one.");
        }

        if (!user.getPasswordResetCode().equals(request.getCode())) {
            throw new InvalidCredentialsException("Invalid or expired reset code.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordResetCode(null);
        user.setPasswordResetExpiry(null);

        userRepository.save(user);
    }

    private User prepareNewUser(BaseUserRequest request, UserRole role) {
        User user = mapRequestToEntity(request);
        user.setRoleType(role.getValue());
        user.setIsActive(false);

        user.setVerificationCode(generateVerificationCode());
        user.setCodeExpiry(LocalDateTime.now().plusMinutes(VERIFICATION_CODE_EXPIRY_MINUTES));

        return user;
    }

    private void validateUniqueness(String email, String nic) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("Email already exists");
        }
        if (userRepository.findByNIC(nic).isPresent()) {
            throw new UserAlreadyExistsException("NIC already exists");
        }
    }

    private Admin getAuthenticatedAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("Authentication required.");
        }

        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("Authenticated user not found."));

        return adminRepository.findById(user.getUserId())
                .orElseThrow(() -> new AccessDeniedException("Current user does not have an Admin profile."));
    }

    private AdminResponse mapToAdminResponse(Admin admin) {
        AdminResponse response = new AdminResponse();
        mapBaseUserToResponse(admin.getUser(), response);
        response.setAdminId(admin.getAdminId());
        response.setDepartment(admin.getDepartment());
        response.setAccessLevel(admin.getAccessLevel());
        return response;
    }

    private StaffResponse mapToStaffResponse(Staff staff) {
        StaffResponse response = new StaffResponse();
        mapBaseUserToResponse(staff.getUser(), response);
        response.setStaffId(staff.getStaffId());
        response.setStatus(staff.getStatus());
        response.setWorkingHours(staff.getWorkingHours());
        response.setSpecialization(staff.getSpecialization());

        return response;
    }

    private PatientResponse mapToPatientResponse(Patient patient) {
        PatientResponse response = new PatientResponse();
        mapBaseUserToResponse(patient.getUser(), response);
        response.setPatientId(patient.getPatientId());
        response.setEmergencyContact(patient.getEmergencyContact());
        response.setBloodGroup(patient.getBloodGroup());
        response.setAllergies(patient.getAllergies());
        return response;
    }

    private DoctorResponse mapToDoctorResponse(Doctor doctor) {
        DoctorResponse response = new DoctorResponse();
        mapBaseUserToResponse(doctor.getUser(), response);
        response.setSpecialization(doctor.getSpecialization());
        response.setLicenseNumber(doctor.getLicenseNumber());
        response.setQualification(doctor.getQualification());
        response.setExperienceYears(doctor.getExperienceYears());
        response.setConsultationFee(doctor.getConsultationFee());
        return response;
    }

    private void mapBaseUserToResponse(User user, UserResponse response) {
        response.setUserId(user.getUserId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPhone(user.getPhone());
        response.setNIC(user.getNIC());
        response.setAddress(user.getAddress());
        response.setIsActive(user.getIsActive());
        response.setRoleType(user.getRoleType());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
    }

    private User mapRequestToEntity(BaseUserRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setNIC(request.getNic());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setGender(request.getGender());
        user.setAddress(request.getAddress());
        return user;
    }

    private String generateVerificationCode() {
        return String.format("%06d", SECURE_RANDOM.nextInt(1_000_000));
    }

}