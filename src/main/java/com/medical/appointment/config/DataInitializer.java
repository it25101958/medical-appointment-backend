package com.medical.appointment.config;

import com.medical.appointment.model.*;
import com.medical.appointment.model.enums.*;
import com.medical.appointment.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final StaffRepository staffRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.findByEmail("superadmin@hospital.com").isEmpty()) {
            createSuperAdmin();
        }

        if (userRepository.findByEmail("doctor@hospital.com").isEmpty()) {
            createDefaultDoctor();
        }

        if (userRepository.findByEmail("staff@hospital.com").isEmpty()) {
            createDefaultStaff();
        }

        if (userRepository.findByEmail("patient@hospital.com").isEmpty()) {
            createDefaultPatient();
        }
    }

    private void createSuperAdmin() {
        User user = createBaseUser("superadmin@hospital.com", "Admin@123", "System", "Admin", UserRole.ADMIN, "000000000V");
        Admin admin = new Admin();
        admin.setUser(user);
        admin.setDepartment("IT");
        admin.setAccessLevel(AccessLevel.SUPER_ADMIN);
        adminRepository.save(admin);
    }

    private void createDefaultDoctor() {
        User user = createBaseUser("doctor@hospital.com", "Doctor@123", "Alice", "Smith", UserRole.DOCTOR, "851234567V");
        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setSpecialization(Specialization.CARDIOLOGY);
        doctor.setLicenseNumber("SLMC-12345");
        doctor.setQualification("MBBS, MD");
        doctor.setExperienceYears(10);
        doctor.setConsultationFee(2500.0);
        doctorRepository.save(doctor);
    }

    private void createDefaultStaff() {
        User user = createBaseUser("staff@hospital.com", "Staff@123", "Bob", "Perera", UserRole.STAFF, "951234567V");
        Staff staff = new Staff();
        staff.setUser(user);
        staff.setStatus(StaffStatus.ACTIVE);
        staff.setWorkingHours("08:00 - 16:00");
        staff.setSpecialization("RECEPTION");
        staffRepository.save(staff);
    }

    private void createDefaultPatient() {
        User user = createBaseUser("patient@hospital.com", "Patient@123", "John", "Doe", UserRole.PATIENT, "901234567V");
        Patient patient = new Patient();
        patient.setUser(user);
        patient.setBloodGroup(BloodGroup.AB_POSITIVE);
        patient.setEmergencyContact("+94771234567");
        patientRepository.save(patient);
    }

    private User createBaseUser(String email, String password, String fName, String lName, UserRole role, String nic) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(fName);
        user.setLastName(lName);
        user.setRoleType(role.getValue());
        user.setNIC(nic);
        user.setPhone("+94710000000");
        user.setDateOfBirth(LocalDate.of(1990, 1, 1));
        user.setGender(Gender.MALE);
        user.setAddress("Hospital Street, Colombo");
        user.setIsActive(true);
        return userRepository.save(user);
    }
}