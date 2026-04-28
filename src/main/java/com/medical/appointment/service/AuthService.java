package com.medical.appointment.service;

import com.medical.appointment.dto.admin.request.CreateAdminRequest;
import com.medical.appointment.dto.auth.request.*;
import com.medical.appointment.dto.auth.response.AuthResponse;
import com.medical.appointment.dto.doctor.request.DoctorRegisterRequest;
import com.medical.appointment.dto.patient.request.PatientRegisterRequest;
import com.medical.appointment.dto.staff.request.StaffRegisterRequest;
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

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Invalid email or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        if (!user.getIsActive()) {
            throw new AccessDeniedException("Account is deactivated. Please contact support.");
        }

        String token = jwtTokenUtil.generateToken(user.getEmail(), String.valueOf(user.getRoleType()));
        return new AuthResponse(token, user);
    }

    @Transactional
    public User registerPatient(PatientRegisterRequest request) {
        validateUniqueness(request.getEmail(), request.getNIC());

        User user = prepareNewUser(request, UserRole.PATIENT);
        user.setRoleType(UserRole.PATIENT.getValue());

        String rawPassword = generateRandomPassword(8);
        user.setPassword(passwordEncoder.encode(rawPassword));

        User savedUser = userRepository.save(user);
        emailService.sendVerificationEmail(savedUser.getEmail(), savedUser.getVerificationCode());

        Patient patient = new Patient();
        patient.setUser(savedUser);
        patient.setEmergencyContact(request.getEmergencyContact());
        patient.setBloodGroup(request.getBloodGroup());
        patient.setAllergies(request.getAllergies());
        patientRepository.save(patient);

        return savedUser;
    }

    @Transactional
    public User registerDoctor(DoctorRegisterRequest request) {
        getAuthenticatedAdmin();
        validateUniqueness(request.getEmail(), request.getNIC());

        User user = prepareNewUser(request, UserRole.DOCTOR);
        user.setRoleType(UserRole.DOCTOR.getValue());
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
        doctorRepository.save(doctor);

        return savedUser;
    }

    @Transactional
    public User registerStaff(StaffRegisterRequest request) {
        getAuthenticatedAdmin();
        validateUniqueness(request.getEmail(), request.getNIC());

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
        staffRepository.save(staff);

        return savedUser;
    }

    @Transactional
    public User registerAdmin(CreateAdminRequest request) {
        Admin loggedInAdmin = getAuthenticatedAdmin();
        if (loggedInAdmin.getAccessLevel() != AccessLevel.SUPER_ADMIN) {
            throw new AccessDeniedException("Only Super Admins can create new Admin accounts.");
        }

        validateUniqueness(request.getEmail(), request.getNIC());

        User user = prepareNewUser(request, UserRole.ADMIN);
        user.setRoleType(UserRole.ADMIN.getValue());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);

        emailService.sendVerificationEmail(savedUser.getEmail(), savedUser.getVerificationCode());

        Admin admin = new Admin();
        admin.setUser(savedUser);
        admin.setDepartment(request.getDepartment());
        admin.setAccessLevel(request.getAccessLevel());
        adminRepository.save(admin);

        return savedUser;
    }

    @Transactional
    public void verifyAccount(VerificationRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));

        if (user.getIsActive()) {
            throw new IllegalStateException("Account is already verified.");
        }

        if (user.getCodeExpiry().isBefore(LocalDateTime.now())) {
            userRepository.delete(user);
            throw new VerificationException("Verification code has expired. Your registration has been reset. Please register again.");
        }

        if (!user.getVerificationCode().equals(request.getCode())) {
            throw new InvalidCredentialsException("Invalid verification code.");
        }

        user.setIsActive(true);
        user.setVerificationCode(null);
        user.setCodeExpiry(null);
        userRepository.save(user);
    }

    private User prepareNewUser(BaseUserRequest request, UserRole role) {
        User user = mapBaseUserFields(request);
        user.setRoleType(role.getValue());
        user.setIsActive(false);

        String otp = String.format("%06d", new Random().nextInt(999999));
        user.setVerificationCode(otp);
        user.setCodeExpiry(LocalDateTime.now().plusMinutes(5));

        return user;
    }

    private void validateUniqueness(String email, String nic) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("Email already exists: " + email);
        }
        if (userRepository.findByNIC(nic).isPresent()) {
            throw new UserAlreadyExistsException("NIC already exists: " + nic);
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

    private User mapBaseUserFields(BaseUserRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setNIC(request.getNIC());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setGender(request.getGender());
        user.setAddress(request.getAddress());
        user.setIsActive(true);
        return user;
    }
}