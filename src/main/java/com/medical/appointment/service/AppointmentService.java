package com.medical.appointment.service;

import com.medical.appointment.dto.appoinment.request.AppointmentCreateRequest;
import com.medical.appointment.dto.appoinment.request.AppointmentStatusUpdateRequest;
import com.medical.appointment.dto.appoinment.request.AppointmentUpdateRequest;
import com.medical.appointment.dto.appoinment.response.AppointmentDoctorSummaryResponse;
import com.medical.appointment.dto.appoinment.response.AppointmentPatientSummaryResponse;
import com.medical.appointment.dto.appoinment.response.AppointmentResponse;
import com.medical.appointment.dto.room.response.RoomResponse;
import com.medical.appointment.model.*;
import com.medical.appointment.model.enums.AccessLevel;
import com.medical.appointment.model.enums.AppointmentStatus;
import com.medical.appointment.model.enums.DayOfWeek;
import com.medical.appointment.repository.*;
import com.medical.appointment.security.SecurityAccessUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.medical.appointment.dto.appoinment.response.AppointmentDoctorSummaryResponse;
import com.medical.appointment.dto.appoinment.response.AppointmentPatientSummaryResponse;
import com.medical.appointment.model.*;
        import com.medical.appointment.model.enums.AccessLevel;
import com.medical.appointment.model.enums.AppointmentStatus;
import com.medical.appointment.model.enums.DayOfWeek;
import com.medical.appointment.dto.feedback.response.FeedbackResponse;
import com.medical.appointment.dto.appoinment.request.AppointmentCreateRequest;
import com.medical.appointment.dto.appoinment.request.AppointmentUpdateRequest;
import com.medical.appointment.dto.appoinment.request.AppointmentStatusUpdateRequest;
import com.medical.appointment.dto.appoinment.response.AppointmentResponse;
import com.medical.appointment.repository.*;
        import com.medical.appointment.security.SecurityAccessUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import com.medical.appointment.dto.room.response.RoomResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final RoomRepository roomRepository;
    private final RoomScheduleRepository roomScheduleRepository;
    private final SecurityAccessUtil securityAccessUtil;
    private final FeedbackRepository feedbackRepository;

    private static final int STANDARD_DURATION_MINUTES = 30;

    @Transactional
    public AppointmentResponse createAppointment(AppointmentCreateRequest request) {
        if (request.getAppointmentDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Appointment date cannot be in the past");
        }

        String currentEmail = securityAccessUtil.getCurrentUserEmail();

        Patient patient = patientRepository.findByUserEmail(currentEmail)
                .orElseThrow(() -> new IllegalArgumentException("Patient profile not found for logged-in user"));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found with ID: " + request.getDoctorId()));

        DayOfWeek operationalDay = DayOfWeek.valueOf(request.getAppointmentDate().getDayOfWeek().name());
        LocalTime startTime = request.getAppointmentTime();
        LocalTime endTime = startTime.plusMinutes(STANDARD_DURATION_MINUTES);

        RoomSchedule doctorShift = roomScheduleRepository.findDoctorActiveShift(doctor, operationalDay, startTime, endTime)
                .orElseThrow(() -> new IllegalStateException(
                        "Doctor " + doctor.getUser().getLastName() + " does not have an active working schedule or room allocated for this time window."));

        Room preAllocatedRoom = doctorShift.getRoom();
        validateDoctorAvailability(request.getDoctorId(), request.getAppointmentDate(), startTime, STANDARD_DURATION_MINUTES, null);

        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(startTime);
        appointment.setDurationMinutes(STANDARD_DURATION_MINUTES);
        appointment.setAppointmentType(request.getAppointmentType());
        appointment.setNotes(request.getNotes());
        appointment.setStatus(AppointmentStatus.PENDING);

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setRoom(preAllocatedRoom); // Seamlessly assigned from Alice's room schedule!

        Integer currentMaxQueue = appointmentRepository.findMaxAppointmentNumberByDoctorAndDate(request.getDoctorId(), request.getAppointmentDate());
        appointment.setAppointmentNumber(currentMaxQueue == null ? 1 : currentMaxQueue + 1);

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return convertToResponseDto(savedAppointment);
    }

    public List<AppointmentResponse> getAllAppointments() {
        String currentEmail = securityAccessUtil.getCurrentUserEmail();
        if (currentEmail == null) {
            throw new org.springframework.security.access.AccessDeniedException("Access Denied: User is not authenticated.");
        }

        boolean isManagementStaff = securityAccessUtil.hasAnyRole("ROLE_SUPER_ADMIN", "ROLE_ADMIN", "ROLE_STAFF");

        List<Appointment> appointments;

        if (isManagementStaff) {
            appointments = appointmentRepository.findAll();
        }
        else if (securityAccessUtil.hasAnyRole("ROLE_DOCTOR")) {
            appointments = appointmentRepository.findAllByDoctorUserEmail(currentEmail);
        }
        else {
            throw new org.springframework.security.access.AccessDeniedException(
                    "Access Denied: You do not have permission to view this appointment index."
            );
        }
        return appointments.stream()
                .map(this::convertToResponseDto)
                .toList();
    }
    public List<LocalTime> getAvailableSlots(Integer doctorId, LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Date cannot be in the past");
        }

        DayOfWeek dayOfWeek = DayOfWeek.valueOf(date.getDayOfWeek().name());

        List<RoomSchedule> schedules =
                roomScheduleRepository.findByDoctor_DoctorIdAndDayOfWeek(doctorId, dayOfWeek);

        if (schedules.isEmpty()) {
            return List.of();
        }

        List<Appointment> bookedAppointments =
                appointmentRepository.findBookedAppointmentsByDoctorAndDate(doctorId, date);

        List<LocalTime> bookedTimes = bookedAppointments.stream()
                .map(Appointment::getAppointmentTime)
                .toList();

        List<LocalTime> availableSlots = new ArrayList<>();

        for (RoomSchedule schedule : schedules) {
            LocalTime slot = schedule.getStartTime();

            while (!slot.plusMinutes(STANDARD_DURATION_MINUTES).isAfter(schedule.getEndTime())) {
                if (!bookedTimes.contains(slot)) {
                    availableSlots.add(slot);
                }

                slot = slot.plusMinutes(STANDARD_DURATION_MINUTES);
            }
        }

        return availableSlots;
    }

    public List<AppointmentResponse> getTodayAppointmentsByDoctor(Integer doctorId) {
        String currentEmail = securityAccessUtil.getCurrentUserEmail();

        if (currentEmail == null) {
            throw new AccessDeniedException("Access Denied: User is not authenticated.");
        }

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found with ID: " + doctorId));

        boolean isManagementStaff = securityAccessUtil.hasAnyRole(
                "ROLE_SUPER_ADMIN",
                "ROLE_ADMIN",
                "ROLE_STAFF"
        );

        boolean isAssignedDoctor = securityAccessUtil.hasAnyRole("ROLE_DOCTOR")
                && securityAccessUtil.isOwner(doctor.getUser().getEmail());

        if (!(isManagementStaff || isAssignedDoctor)) {
            throw new AccessDeniedException(
                    "Access Denied: You do not have permission to view this doctor's appointments."
            );
        }

        LocalDate today = LocalDate.now();

        return appointmentRepository
                .findByDoctorDoctorIdAndAppointmentDateAndStatusNotOrderByAppointmentTimeAsc(
                        doctorId,
                        today,
                        AppointmentStatus.CANCELLED
                )
                .stream()
                .map(this::convertToResponseDto)
                .toList();
    }

    public List<AppointmentResponse> getMyAppointments() {
        String currentEmail = securityAccessUtil.getCurrentUserEmail();

        if (currentEmail == null) {
            throw new AccessDeniedException("Access Denied: User is not authenticated.");
        }

        List<Appointment> appointments;

        if (securityAccessUtil.hasAnyRole("ROLE_PATIENT")) {
            appointments = appointmentRepository
                    .findAllByPatientUserEmailOrderByAppointmentDateDescAppointmentTimeDesc(currentEmail);
        }
        else if (securityAccessUtil.hasAnyRole("ROLE_DOCTOR")) {
            appointments = appointmentRepository
                    .findAllByDoctorUserEmailOrderByAppointmentDateDescAppointmentTimeDesc(currentEmail);
        }
        else if (securityAccessUtil.hasAnyRole("ROLE_SUPER_ADMIN", "ROLE_ADMIN", "ROLE_STAFF")) {
            appointments = appointmentRepository.findAll();
        }
        else {
            throw new AccessDeniedException(
                    "Access Denied: You do not have permission to view appointments."
            );
        }

        return appointments.stream()
                .map(this::convertToResponseDto)
                .toList();
    }

    public List<AppointmentResponse> getMyTodayAppointments() {
        securityAccessUtil.validateDoctorAccess();

        String currentEmail = securityAccessUtil.getCurrentUserEmail();

        if (currentEmail == null) {
            throw new AccessDeniedException("Access Denied: User is not authenticated.");
        }

        LocalDate today = LocalDate.now();

        return appointmentRepository
                .findByDoctorUserEmailAndAppointmentDateAndStatusNotOrderByAppointmentTimeAsc(
                        currentEmail,
                        today,
                        AppointmentStatus.CANCELLED
                )
                .stream()
                .map(this::convertToResponseDto)
                .toList();
    }

    public AppointmentResponse getAppointmentById(Integer appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + appointmentId));

        String currentUserEmail = securityAccessUtil.getCurrentUserEmail();

        boolean isSuperAdmin = securityAccessUtil.hasAnyRole("ROLE_SUPER_ADMIN");
        boolean isStaff = securityAccessUtil.hasAnyRole("ROLE_STAFF");
        boolean isOwner = securityAccessUtil.isOwner(appointment.getPatient().getUser().getEmail());
        boolean isAssignedDoctor = securityAccessUtil.isOwner(appointment.getDoctor().getUser().getEmail());

        if (!(isSuperAdmin || isStaff || isAssignedDoctor || isOwner)) {
            throw new AccessDeniedException(
                    "Access Denied: You do not have permission to view this appointment record."
            );
        }
        return convertToResponseDto(appointment);
    }

    @Transactional
    public AppointmentResponse updateAppointment(Integer appointmentId, AppointmentUpdateRequest request) {
        verifyAdminOrStaffAccess();

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + appointmentId));

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Completed appointments cannot be updated");
        }

        LocalDate targetDate = request.getAppointmentDate() != null ? request.getAppointmentDate() : appointment.getAppointmentDate();
        LocalTime targetTime = request.getAppointmentTime() != null ? request.getAppointmentTime() : appointment.getAppointmentTime();
        Integer targetDuration = request.getDurationMinutes() != null ? request.getDurationMinutes() : appointment.getDurationMinutes();

        if (request.getAppointmentDate() != null || request.getAppointmentTime() != null || request.getDurationMinutes() != null) {
            validateDoctorAvailability(appointment.getDoctor().getDoctorId(), targetDate, targetTime, targetDuration, appointmentId);

            DayOfWeek targetDay = DayOfWeek.valueOf(targetDate.getDayOfWeek().name());
            LocalTime targetEndTime = targetTime.plusMinutes(targetDuration);

            RoomSchedule updatedShift = roomScheduleRepository.findDoctorActiveShift(appointment.getDoctor(), targetDay, targetTime, targetEndTime)
                    .orElseThrow(() -> new IllegalStateException("Doctor does not have a shift schedule or room pre-allocated for the modified time block."));

            appointment.setRoom(updatedShift.getRoom());

            if (request.getAppointmentDate() != null && !request.getAppointmentDate().equals(appointment.getAppointmentDate())) {
                Integer currentMaxQueue = appointmentRepository.findMaxAppointmentNumberByDoctorAndDate(appointment.getDoctor().getDoctorId(), targetDate);
                appointment.setAppointmentNumber(currentMaxQueue == null ? 1 : currentMaxQueue + 1);
            }

            appointment.setAppointmentDate(targetDate);
            appointment.setAppointmentTime(targetTime);
            appointment.setDurationMinutes(targetDuration);
        }

        if (request.getAppointmentType() != null) {
            appointment.setAppointmentType(request.getAppointmentType());
        }

        if (request.getNotes() != null) {
            appointment.setNotes(request.getNotes());
        }

        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return convertToResponseDto(updatedAppointment);
    }

    @Transactional
    public AppointmentResponse updateAppointmentStatus(Integer appointmentId, AppointmentStatusUpdateRequest request) {
        verifyAdminOrStaffAccess();

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + appointmentId));

        appointment.setStatus(request.getStatus());
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return convertToResponseDto(updatedAppointment);
    }

    @Transactional
    public void cancelAppointment(Integer appointmentId) {
        verifyAdminOrStaffAccess();

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + appointmentId));

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    private void verifyAdminOrStaffAccess() {
        try {
            securityAccessUtil.validateAdminLevel(AccessLevel.SUPER_ADMIN, AccessLevel.FULL);
        } catch (AccessDeniedException adminException) {
            try {
                securityAccessUtil.validateStaffStatus(
                        com.medical.appointment.model.enums.StaffStatus.ACTIVE
                );
            } catch (AccessDeniedException staffException) {
                throw new AccessDeniedException(
                        "Access Denied: Only Admin and Staff personnel are permitted to modify appointments."
                );
            }
        }
    }

    private void validateDoctorAvailability(Integer doctorId, LocalDate date, LocalTime startTime, Integer durationMinutes, Integer currentAppointmentId) {
        LocalTime endTime = startTime.plusMinutes(durationMinutes);
        List<Appointment> conflicts = appointmentRepository.findConflictingAppointments(doctorId, date, startTime, endTime);

        long realConflicts = conflicts.stream()
                .filter(a -> !a.getAppointmentId().equals(currentAppointmentId))
                .count();

        if (realConflicts > 0) {
            throw new IllegalStateException("Doctor already has an overlapping appointment during this time slot.");
        }
    }

    private AppointmentResponse convertToResponseDto(Appointment appointment) {
        AppointmentResponse dto = new AppointmentResponse();

        dto.setAppointmentId(appointment.getAppointmentId());
        dto.setAppointmentNumber(appointment.getAppointmentNumber());
        dto.setAppointmentDate(appointment.getAppointmentDate());
        dto.setAppointmentTime(appointment.getAppointmentTime());
        dto.setDurationMinutes(appointment.getDurationMinutes());
        dto.setAppointmentType(appointment.getAppointmentType());
        dto.setNotes(appointment.getNotes());
        dto.setStatus(appointment.getStatus());
        dto.setCreatedAt(appointment.getCreatedAt());
        dto.setUpdatedAt(appointment.getUpdatedAt());

        if (appointment.getPatient() != null) {
            dto.setPatientId(appointment.getPatient().getPatientId());
            dto.setPatient(mapPatientSummary(appointment.getPatient()));
        }

        if (appointment.getDoctor() != null) {
            dto.setDoctorId(appointment.getDoctor().getDoctorId());
            dto.setDoctor(mapDoctorSummary(appointment.getDoctor()));
        }

        if (appointment.getRoom() != null) {
            dto.setRoomId(appointment.getRoom().getRoomId());
            dto.setRoom(mapRoomResponse(appointment.getRoom()));
        }

        if (canCurrentUserViewAppointmentFeedback(appointment)) {
            List<com.medical.appointment.dto.feedback.response.FeedbackResponse> feedbacks = feedbackRepository
                    .findByAppointmentAppointmentId(appointment.getAppointmentId())
                    .stream()
                    .map(this::mapFeedbackToResponse)
                    .toList();

            dto.setFeedbacks(feedbacks);
        }

        return dto;
    }

    private AppointmentPatientSummaryResponse mapPatientSummary(Patient patient) {
        User user = patient.getUser();

        return AppointmentPatientSummaryResponse.builder()
                .patientId(patient.getPatientId())
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFirstName() + " " + user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .bloodGroup(patient.getBloodGroup())
                .allergies(patient.getAllergies())
                .build();
    }

    private AppointmentDoctorSummaryResponse mapDoctorSummary(Doctor doctor) {
        User user = doctor.getUser();

        return AppointmentDoctorSummaryResponse.builder()
                .doctorId(doctor.getDoctorId())
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName("Dr. " + user.getFirstName() + " " + user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .specialization(doctor.getSpecialization())
                .qualification(doctor.getQualification())
                .experienceYears(doctor.getExperienceYears())
                .consultationFee(doctor.getConsultationFee())
                .build();
    }

    private RoomResponse mapRoomResponse(Room room) {
        return new RoomResponse(
                room.getRoomId(),
                room.getRoomNumber(),
                room.getRoomType(),
                room.getCapacity(),
                room.getStatus(),
                room.getEquipmentAvailable()
        );
    }

    private boolean canCurrentUserViewAppointmentFeedback(Appointment appointment) {
        boolean isPatientOwner = securityAccessUtil.isOwner(
                appointment.getPatient().getUser().getEmail()
        );

        boolean isAssignedDoctor = securityAccessUtil.isOwner(
                appointment.getDoctor().getUser().getEmail()
        );

        boolean isAdmin = securityAccessUtil.hasAnyRole(
                "ROLE_SUPER_ADMIN",
                "ROLE_ADMIN"
        );

        return isPatientOwner || isAssignedDoctor || isAdmin;
    }

    private com.medical.appointment.dto.feedback.response.FeedbackResponse mapFeedbackToResponse(Feedback feedback) {
        return new com.medical.appointment.dto.feedback.response.FeedbackResponse(
                feedback.getFeedbackId(),
                feedback.getAppointment().getAppointmentId(),
                feedback.getPatient().getPatientId(),
                feedback.getPatient().getUser().getFirstName() + " " + feedback.getPatient().getUser().getLastName(),
                feedback.getDoctor().getDoctorId(),
                "Dr. " + feedback.getDoctor().getUser().getFirstName() + " " + feedback.getDoctor().getUser().getLastName(),
                feedback.getRating(),
                feedback.getComments(),
                feedback.getStatus(),
                feedback.getCreatedAt()
        );
    }
}