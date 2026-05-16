package com.medical.appointment.config;

import com.medical.appointment.model.*;
import com.medical.appointment.model.enums.*;
import com.medical.appointment.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final String DEFAULT_PASSWORD = "Password@123";

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final StaffRepository staffRepository;
    private final PatientRepository patientRepository;

    private final RoomRepository roomRepository;
    private final RoomScheduleRepository roomScheduleRepository;
    private final AppointmentRepository appointmentRepository;

    private final MedicationRepository medicationRepository;
    private final PrescriptionRepository prescriptionRepository;

    private final LaboratoryRepository laboratoryRepository;
    private final LabTestRepository labTestRepository;
    private final LabOrderRepository labOrderRepository;

    private final BillingRepository billingRepository;
    private final FeedbackRepository feedbackRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {

        Admin superAdmin = getOrCreateSuperAdmin();

        Admin hrAdmin = getOrCreateAdmin(
                "admin_hr@hospital.com",
                "Admin",
                "HR",
                "870000001V",
                "HR",
                AccessLevel.FULL
        );

        Admin financeAdmin = getOrCreateAdmin(
                "admin_finance@hospital.com",
                "Admin",
                "Finance",
                "870000002V",
                "Finance",
                AccessLevel.FULL
        );

        Doctor doctor1 = getOrCreateDoctor(
                "doctor@hospital.com",
                "Alice",
                "Smith",
                "851234567V",
                Specialization.CARDIOLOGY,
                "SLMC-12345",
                "MBBS, MD",
                10,
                2500.0
        );

        Doctor doctor2 = getOrCreateDoctor(
                "neurologist@hospital.com",
                "Jane",
                "Doe",
                "861234567V",
                Specialization.NEUROLOGY,
                "SLMC-99999",
                "MBBS, MS",
                8,
                3000.0
        );

        Doctor doctor3 = getOrCreateDoctor(
                "pediatrician@hospital.com",
                "Mark",
                "Wilson",
                "881234567V",
                Specialization.PEDIATRICS,
                "SLMC-88888",
                "MBBS, DCH",
                6,
                2200.0
        );

        Staff staff1 = getOrCreateStaff(
                "staff@hospital.com",
                "Bob",
                "Perera",
                "951234567V",
                StaffStatus.ACTIVE,
                "08:00 - 16:00",
                "RECEPTION"
        );

        Staff staff2 = getOrCreateStaff(
                "nurse@hospital.com",
                "Sarah",
                "Connor",
                "961234567V",
                StaffStatus.ACTIVE,
                "14:00 - 22:00",
                "NURSING"
        );

        Staff staff3 = getOrCreateStaff(
                "labtech@hospital.com",
                "Mike",
                "Ross",
                "971234567V",
                StaffStatus.ACTIVE,
                "08:00 - 16:00",
                "LABORATORY"
        );

        Patient patient1 = getOrCreatePatient(
                "patient@hospital.com",
                "John",
                "Doe",
                "901234567V",
                BloodGroup.AB_POSITIVE,
                "+94771234567",
                "None"
        );

        Patient patient2 = getOrCreatePatient(
                "patient2@hospital.com",
                "Emily",
                "Clark",
                "921234567V",
                BloodGroup.O_POSITIVE,
                "+94779876543",
                "Penicillin allergy"
        );

        Patient patient3 = getOrCreatePatient(
                "patient3@hospital.com",
                "David",
                "Miller",
                "881234568V",
                BloodGroup.A_NEGATIVE,
                "+94775556677",
                "Dust allergy"
        );

        Room room1 = getOrCreateRoom(
                "R-101",
                "Consultation",
                1,
                RoomStatus.AVAILABLE,
                "Standard Stethoscope, Blood Pressure Monitor"
        );

        Room room2 = getOrCreateRoom(
                "R-102",
                "Consultation",
                1,
                RoomStatus.AVAILABLE,
                "ECG Machine, Blood Pressure Monitor"
        );

        Room room3 = getOrCreateRoom(
                "LAB-01",
                "Laboratory",
                3,
                RoomStatus.AVAILABLE,
                "Microscope, Blood Sample Analyzer"
        );

        createRoomSchedulesIfEmpty(room1, room2, doctor1, doctor2);

        Appointment appointment1 = getOrCreateAppointment(
                patient1,
                doctor1,
                room1,
                LocalDate.now().plusDays(2),
                LocalTime.of(14, 0),
                1,
                "Initial cardiology consultation."
        );

        Appointment appointment2 = getOrCreateAppointment(
                patient2,
                doctor2,
                room2,
                LocalDate.now().plusDays(3),
                LocalTime.of(10, 30),
                2,
                "Neurology consultation for headache symptoms."
        );

        Medication med1 = getOrCreateMedication(
                "Paracetamol",
                "Acetaminophen",
                "ABC Pharma",
                "500mg",
                "Tablet"
        );

        Medication med2 = getOrCreateMedication(
                "Amoxicillin",
                "Amoxicillin",
                "HealthCare Pharma",
                "250mg",
                "Capsule"
        );

        Medication med3 = getOrCreateMedication(
                "Cetirizine",
                "Cetirizine Hydrochloride",
                "MediLife Pharma",
                "10mg",
                "Tablet"
        );

        createPrescriptionIfEmpty(appointment1, doctor1, patient1, med1, med2);
        createPrescriptionIfEmpty(appointment2, doctor2, patient2, med3, med1);

        Laboratory laboratory = getOrCreateLaboratory();

        LabTest test1 = getOrCreateLabTest(
                "Full Blood Count",
                "Haematology",
                "Measures red cells, white cells, haemoglobin and platelets.",
                new BigDecimal("1200.00")
        );

        LabTest test2 = getOrCreateLabTest(
                "Blood Sugar Fasting",
                "Biochemistry",
                "Measures fasting blood glucose level.",
                new BigDecimal("800.00")
        );

        LabTest test3 = getOrCreateLabTest(
                "Lipid Profile",
                "Biochemistry",
                "Measures cholesterol and triglyceride levels.",
                new BigDecimal("2500.00")
        );

        createLabOrderIfEmpty(appointment1, laboratory, test1, test2);
        createLabOrderIfEmpty(appointment2, laboratory, test2, test3);

        createBillingIfEmpty(appointment1, patient1, new BigDecimal("4500.00"));
        createBillingIfEmpty(appointment2, patient2, new BigDecimal("5200.00"));

        createFeedbackIfEmpty(appointment1, patient1, doctor1);
        createFeedbackIfEmpty(appointment2, patient2, doctor2);
    }

    private Admin getOrCreateSuperAdmin() {
        return getOrCreateAdmin(
                "superadmin@hospital.com",
                "System",
                "Admin",
                "000000000V",
                "IT",
                AccessLevel.SUPER_ADMIN
        );
    }

    private Admin getOrCreateAdmin(
            String email,
            String firstName,
            String lastName,
            String nic,
            String department,
            AccessLevel accessLevel
    ) {
        return adminRepository.findByUserEmail(email)
                .orElseGet(() -> {
                    User user = getOrCreateBaseUser(
                            email,
                            firstName,
                            lastName,
                            UserRole.ADMIN,
                            nic
                    );

                    Admin admin = new Admin();
                    admin.setUser(user);
                    admin.setDepartment(department);
                    admin.setAccessLevel(accessLevel);

                    return adminRepository.save(admin);
                });
    }

    private Doctor getOrCreateDoctor(
            String email,
            String firstName,
            String lastName,
            String nic,
            Specialization specialization,
            String licenseNumber,
            String qualification,
            Integer experienceYears,
            Double consultationFee
    ) {
        return userRepository.findByEmail(email)
                .flatMap(user -> doctorRepository.findById(user.getUserId()))
                .orElseGet(() -> {
                    User user = getOrCreateBaseUser(
                            email,
                            firstName,
                            lastName,
                            UserRole.DOCTOR,
                            nic
                    );

                    Doctor doctor = new Doctor();
                    doctor.setUser(user);
                    doctor.setSpecialization(specialization);
                    doctor.setLicenseNumber(licenseNumber);
                    doctor.setQualification(qualification);
                    doctor.setExperienceYears(experienceYears);
                    doctor.setConsultationFee(consultationFee);

                    return doctorRepository.save(doctor);
                });
    }

    private Staff getOrCreateStaff(
            String email,
            String firstName,
            String lastName,
            String nic,
            StaffStatus status,
            String workingHours,
            String specialization
    ) {
        return userRepository.findByEmail(email)
                .flatMap(user -> staffRepository.findById(user.getUserId()))
                .orElseGet(() -> {
                    User user = getOrCreateBaseUser(
                            email,
                            firstName,
                            lastName,
                            UserRole.STAFF,
                            nic
                    );

                    Staff staff = new Staff();
                    staff.setUser(user);
                    staff.setStatus(status);
                    staff.setWorkingHours(workingHours);
                    staff.setSpecialization(specialization);

                    return staffRepository.save(staff);
                });
    }

    private Patient getOrCreatePatient(
            String email,
            String firstName,
            String lastName,
            String nic,
            BloodGroup bloodGroup,
            String emergencyContact,
            String allergies
    ) {
        return userRepository.findByEmail(email)
                .flatMap(user -> patientRepository.findById(user.getUserId()))
                .orElseGet(() -> {
                    User user = getOrCreateBaseUser(
                            email,
                            firstName,
                            lastName,
                            UserRole.PATIENT,
                            nic
                    );

                    Patient patient = new Patient();
                    patient.setUser(user);
                    patient.setBloodGroup(bloodGroup);
                    patient.setEmergencyContact(emergencyContact);
                    patient.setAllergies(allergies);

                    return patientRepository.save(patient);
                });
    }

    private User getOrCreateBaseUser(
            String email,
            String firstName,
            String lastName,
            UserRole role,
            String nic
    ) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User user = new User();
                    user.setEmail(email);
                    user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setRoleType(role.getValue());
                    user.setNIC(nic);
                    user.setPhone("+94710000000");
                    user.setDateOfBirth(LocalDate.of(1990, 1, 1));
                    user.setGender(Gender.MALE);
                    user.setAddress("Hospital Street, Colombo");
                    user.setIsActive(true);
                    user.setVerificationCode(null);
                    user.setCodeExpiry(null);

                    return userRepository.save(user);
                });
    }

    private Room getOrCreateRoom(
            String roomNumber,
            String roomType,
            Integer capacity,
            RoomStatus status,
            String equipment
    ) {
        return roomRepository.findByRoomNumber(roomNumber)
                .orElseGet(() -> {
                    Room room = new Room();
                    room.setRoomNumber(roomNumber);
                    room.setRoomType(roomType);
                    room.setCapacity(capacity);
                    room.setStatus(status);
                    room.setEquipmentAvailable(equipment);

                    return roomRepository.save(room);
                });
    }

    private void createRoomSchedulesIfEmpty(
            Room room1,
            Room room2,
            Doctor doctor1,
            Doctor doctor2
    ) {
        if (roomScheduleRepository.count() > 0) {
            return;
        }

        RoomSchedule schedule1 = new RoomSchedule();
        schedule1.setRoom(room1);
        schedule1.setDoctor(doctor1);
        schedule1.setDayOfWeek(DayOfWeek.MONDAY);
        schedule1.setStartTime(LocalTime.of(9, 0));
        schedule1.setEndTime(LocalTime.of(17, 0));
        roomScheduleRepository.save(schedule1);

        RoomSchedule schedule2 = new RoomSchedule();
        schedule2.setRoom(room2);
        schedule2.setDoctor(doctor2);
        schedule2.setDayOfWeek(DayOfWeek.TUESDAY);
        schedule2.setStartTime(LocalTime.of(8, 30));
        schedule2.setEndTime(LocalTime.of(16, 30));
        roomScheduleRepository.save(schedule2);
    }

    private Appointment getOrCreateAppointment(
            Patient patient,
            Doctor doctor,
            Room room,
            LocalDate date,
            LocalTime time,
            Integer appointmentNumber,
            String notes
    ) {
        List<Appointment> existingAppointments = appointmentRepository.findAll();

        for (Appointment existing : existingAppointments) {
            if (existing.getPatient().getPatientId().equals(patient.getPatientId())
                    && existing.getDoctor().getDoctorId().equals(doctor.getDoctorId())
                    && existing.getAppointmentDate().equals(date)
                    && existing.getAppointmentTime().equals(time)) {
                return existing;
            }
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setRoom(room);
        appointment.setAppointmentDate(date);
        appointment.setAppointmentTime(time);
        appointment.setAppointmentNumber(appointmentNumber);
        appointment.setDurationMinutes(30);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment.setAppointmentType(AppointmentType.CONSULTATION);
        appointment.setNotes(notes);

        return appointmentRepository.save(appointment);
    }

    private Medication getOrCreateMedication(
            String name,
            String genericName,
            String manufacturer,
            String dosage,
            String dosageForm
    ) {
        return medicationRepository.findByName(name)
                .orElseGet(() -> {
                    Medication medication = new Medication();
                    medication.setName(name);
                    medication.setGenericName(genericName);
                    medication.setManufacturer(manufacturer);
                    medication.setDosage(dosage);
                    medication.setDosageForm(dosageForm);

                    /*
                     * This avoids depending on the exact enum value name.
                     * If your MedicationStatus has ACTIVE or AVAILABLE, you may replace this.
                     */
                    medication.setStatus(MedicationStatus.values()[0]);

                    return medicationRepository.save(medication);
                });
    }

    private void createPrescriptionIfEmpty(
            Appointment appointment,
            Doctor doctor,
            Patient patient,
            Medication med1,
            Medication med2
    ) {
        if (prescriptionRepository.existsByAppointmentAppointmentId(appointment.getAppointmentId())) {
            return;
        }

        Prescription prescription = new Prescription();
        prescription.setAppointment(appointment);
        prescription.setDoctor(doctor);
        prescription.setPatient(patient);
        prescription.setPrescriptionDate(LocalDate.now());
        prescription.setStatus(PrescriptionStatus.ACTIVE);
        prescription.setNotes("Take medicines after meals.");

        PrescriptionItem item1 = new PrescriptionItem();
        item1.setPrescription(prescription);
        item1.setMedication(med1);
        item1.setDosage("1 tablet twice daily");
        item1.setQuantity(10);
        item1.setSpecialInstructions("Take after food.");

        PrescriptionItem item2 = new PrescriptionItem();
        item2.setPrescription(prescription);
        item2.setMedication(med2);
        item2.setDosage("1 capsule once daily");
        item2.setQuantity(7);
        item2.setSpecialInstructions("Take with water.");

        prescription.setItems(List.of(item1, item2));

        prescriptionRepository.save(prescription);
    }

    private Laboratory getOrCreateLaboratory() {
        if (laboratoryRepository.count() > 0) {
            return laboratoryRepository.findAll().get(0);
        }

        Laboratory laboratory = new Laboratory();
        laboratory.setName("Central Laboratory");
        laboratory.setAddress("Hospital Main Building, Colombo");
        laboratory.setOpeningHours("08:00 - 18:00");
        laboratory.setPhone("+94712223344");
        laboratory.setEmail("lab@hospital.com");

        return laboratoryRepository.save(laboratory);
    }

    private LabTest getOrCreateLabTest(
            String testName,
            String category,
            String description,
            BigDecimal price
    ) {
        return labTestRepository.findByTestName(testName)
                .orElseGet(() -> {
                    LabTest labTest = new LabTest();
                    labTest.setTestName(testName);
                    labTest.setCategory(category);
                    labTest.setDescription(description);
                    labTest.setStandardPrice(price);
                    labTest.setIsActive(true);

                    return labTestRepository.save(labTest);
                });
    }

    private void createLabOrderIfEmpty(
            Appointment appointment,
            Laboratory laboratory,
            LabTest test1,
            LabTest test2
    ) {
        if (labOrderRepository.existsByAppointmentAppointmentId(appointment.getAppointmentId())) {
            return;
        }

        LabOrder labOrder = new LabOrder();
        labOrder.setAppointment(appointment);
        labOrder.setLaboratory(laboratory);

        LabOrderItem item1 = new LabOrderItem();
        item1.setLabOrder(labOrder);
        item1.setLabTest(test1);
        item1.setQuantity(1);
        item1.setUnitPrice(test1.getStandardPrice());
        item1.calculateTotalPrice();
        item1.setStatus("PENDING");

        LabOrderItem item2 = new LabOrderItem();
        item2.setLabOrder(labOrder);
        item2.setLabTest(test2);
        item2.setQuantity(1);
        item2.setUnitPrice(test2.getStandardPrice());
        item2.calculateTotalPrice();
        item2.setStatus("PENDING");

        labOrder.setItems(List.of(item1, item2));

        labOrderRepository.save(labOrder);
    }

    private void createBillingIfEmpty(
            Appointment appointment,
            Patient patient,
            BigDecimal totalAmount
    ) {
        List<Billing> existingBillings = billingRepository
                .findByAppointment_AppointmentId(appointment.getAppointmentId());

        if (!existingBillings.isEmpty()) {
            return;
        }

        BigDecimal discount = new BigDecimal("500.00");
        BigDecimal tax = new BigDecimal("200.00");
        BigDecimal finalAmount = totalAmount.subtract(discount).add(tax);

        Billing billing = new Billing();
        billing.setAppointment(appointment);
        billing.setPatient(patient);

        // Required field
        billing.setBillingDate(LocalDate.now());

        billing.setTotalAmount(totalAmount);
        billing.setDiscount(discount);
        billing.setTax(tax);
        billing.setFinalAmount(finalAmount);
        billing.setDueDate(LocalDate.now().plusDays(7));
        billing.setStatus(BillingStatus.PENDING);

        billingRepository.save(billing);
    }

    private void createFeedbackIfEmpty(
            Appointment appointment,
            Patient patient,
            Doctor doctor
    ) {
        if (feedbackRepository.count() >= 2) {
            return;
        }

        Feedback feedback = new Feedback();
        feedback.setAppointment(appointment);
        feedback.setPatient(patient);
        feedback.setDoctor(doctor);
        feedback.setRating(5);
        feedback.setComments("Good consultation and friendly staff.");
        feedback.setStatus(FeedbackStatus.PENDING);

        feedbackRepository.save(feedback);
    }
}