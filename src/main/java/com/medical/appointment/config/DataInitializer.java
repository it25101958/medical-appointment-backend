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
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final StaffRepository staffRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoomRepository roomRepository;
    private final AppointmentRepository appointmentRepository;

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

        // Additional

        if (userRepository.findByEmail("admin_hr@hospital.com").isEmpty()) {
            createAdditionalAdmin("admin_hr@hospital.com", "HR", AccessLevel.FULL);
        }
        if (userRepository.findByEmail("admin_finance@hospital.com").isEmpty()) {
            createAdditionalAdmin("admin_finance@hospital.com", "Finance", AccessLevel.FULL);
        }

        // Additional Doctors
        if (userRepository.findByEmail("neurologist@hospital.com").isEmpty()) {
            createAdditionalDoctor("neurologist@hospital.com", "Jane", "Doe", Specialization.NEUROLOGY, "SLMC-99999");
        }
        if (userRepository.findByEmail("pediatrician@hospital.com").isEmpty()) {
            createAdditionalDoctor("pediatrician@hospital.com", "Mark", "Wilson", Specialization.PEDIATRICS, "SLMC-88888");
        }

        // Additional Staff
        if (userRepository.findByEmail("nurse@hospital.com").isEmpty()) {
            createAdditionalStaff("nurse@hospital.com", "Sarah", "Connor", StaffStatus.ACTIVE, "NURSING");
        }
        if (userRepository.findByEmail("labtech@hospital.com").isEmpty()) {
            createAdditionalStaff("labtech@hospital.com", "Mike", "Ross", StaffStatus.ON_LEAVE, "LABORATORY");
        }

        // Additional Patients
        if (userRepository.findByEmail("patient2@hospital.com").isEmpty()) {
            createAdditionalPatient("patient2@hospital.com", "Emily", "Clark", BloodGroup.O_POSITIVE, "921234567V");
        }
        if (userRepository.findByEmail("patient3@hospital.com").isEmpty()) {
            createAdditionalPatient("patient3@hospital.com", "David", "Miller", BloodGroup.A_NEGATIVE, "881234567V");
        }

        Room room = createDefaultRoom();

        Patient patient = patientRepository.findAll().stream()
                .findFirst()
                .orElseGet(this::createDefaultPatient);

        Doctor doctor = doctorRepository.findAll().stream()
                .findFirst()
                .orElseGet(this::createDefaultDoctor);

        if (appointmentRepository.count() == 0) {
            createDefaultAppointment(patient, doctor, room);
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

    private Doctor createDefaultDoctor() {
        User user = createBaseUser("doctor@hospital.com", "Doctor@123", "Alice", "Smith", UserRole.DOCTOR, "851234567V");
        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setSpecialization(Specialization.CARDIOLOGY);
        doctor.setLicenseNumber("SLMC-12345");
        doctor.setQualification("MBBS, MD");
        doctor.setExperienceYears(10);
        doctor.setConsultationFee(2500.0);
        return doctorRepository.save(doctor);
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

    private Patient createDefaultPatient() {
        User user = createBaseUser("patient@hospital.com", "Patient@123", "John", "Doe", UserRole.PATIENT, "901234567V");
        Patient patient = new Patient();
        patient.setUser(user);
        patient.setBloodGroup(BloodGroup.AB_POSITIVE);
        patient.setEmergencyContact("+94771234567");
        return patientRepository.save(patient);
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

    private Room createDefaultRoom() {
        return roomRepository.findByRoomNumber("R-101")
                .orElseGet(() -> {
                    Room room = new Room();
                    room.setRoomNumber("R-101");
                    room.setRoomType("Consultation");
                    room.setCapacity(1);
                    room.setStatus(RoomStatus.AVAILABLE);
                    room.setEquipmentAvailable("Standard Stethoscope, Blood Pressure Monitor");
                    return roomRepository.save(room);
                });
    }

    private void createDefaultAppointment(Patient patient, Doctor doctor, Room room) {
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setRoom(room);
        appointment.setAppointmentDate(LocalDate.now().plusDays(2));
        appointment.setAppointmentTime(LocalTime.of(14, 0));
        appointment.setAppointmentNumber(1);
        appointment.setDurationMinutes(30);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment.setAppointmentType(AppointmentType.CONSULTATION);
        appointment.setNotes("Initial cardiology consultation.");
        appointmentRepository.save(appointment);
    }

    private void createAdditionalAdmin(String email, String dept, AccessLevel level) {
        User user = createBaseUser(email, "Admin@123", "Admin", dept, UserRole.ADMIN, generateFakeNic());
        Admin admin = new Admin();
        admin.setUser(user);
        admin.setDepartment(dept);
        admin.setAccessLevel(level);
        adminRepository.save(admin);
    }

    private void createAdditionalDoctor(String email, String fName, String lName, Specialization spec, String license) {
        User user = createBaseUser(email, "Doctor@123", fName, lName, UserRole.DOCTOR, generateFakeNic());
        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setSpecialization(spec);
        doctor.setLicenseNumber(license);
        doctor.setQualification("MBBS, MS");
        doctor.setExperienceYears(8);
        doctor.setConsultationFee(3000.0);
        doctorRepository.save(doctor);
    }

    private void createAdditionalStaff(String email, String fName, String lName, StaffStatus status, String spec) {
        User user = createBaseUser(email, "Staff@123", fName, lName, UserRole.STAFF, generateFakeNic());
        Staff staff = new Staff();
        staff.setUser(user);
        staff.setStatus(status);
        staff.setWorkingHours("14:00 - 22:00");
        staff.setSpecialization(spec);
        staffRepository.save(staff);
    }

    private void createAdditionalPatient(String email, String fName, String lName, BloodGroup bg, String nic) {
        User user = createBaseUser(email, "Patient@123", fName, lName, UserRole.PATIENT, nic);
        Patient patient = new Patient();
        patient.setUser(user);
        patient.setBloodGroup(bg);
        patient.setEmergencyContact("+94779876543");
        patient.setAllergies("None");
        patientRepository.save(patient);
    }

    private String generateFakeNic() {
        long number = 100_000_000L + (long) (Math.random() * 900_000_000L);
        return number + "V";
    }


}