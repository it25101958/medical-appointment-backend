package com.medical.appointment.service;

import com.medical.appointment.dto.feedback.request.FeedbackRequest;
import com.medical.appointment.dto.feedback.request.FeedbackUpdateRequest;
import com.medical.appointment.dto.feedback.response.FeedbackResponse;
import com.medical.appointment.model.Appointment;
import com.medical.appointment.model.Doctor;
import com.medical.appointment.model.Feedback;
import com.medical.appointment.model.Patient;
import com.medical.appointment.model.enums.AccessLevel;
import com.medical.appointment.model.enums.FeedbackStatus;
import com.medical.appointment.repository.AppointmentRepository;
import com.medical.appointment.repository.DoctorRepository;
import com.medical.appointment.repository.FeedbackRepository;
import com.medical.appointment.repository.PatientRepository;
import com.medical.appointment.security.SecurityAccessUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final SecurityAccessUtil securityAccessUtil;

    @Transactional
    public FeedbackResponse createFeedback(FeedbackRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        securityAccessUtil.validateModificationAccess(patient.getUser().getEmail());

        Feedback feedback = new Feedback();
        feedback.setPatient(patient);
        feedback.setDoctor(doctor);
        feedback.setAppointment(appointment);
        feedback.setRating(request.getRating());
        feedback.setComments(request.getComments());
        feedback.setStatus(FeedbackStatus.PENDING);

        return mapToResponse(feedbackRepository.save(feedback));
    }

    @Transactional
    public FeedbackResponse updateFeedback(int id, FeedbackUpdateRequest updateRequest) {
        Feedback existing = feedbackRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Feedback not found"));

        securityAccessUtil.validateModificationAccess(existing.getPatient().getUser().getEmail());

        if (existing.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(20))) {
            throw new IllegalStateException("Edit window expired (20 mins).");
        }

        existing.setRating(updateRequest.getRating());
        existing.setComments(updateRequest.getComments());

        return mapToResponse(feedbackRepository.save(existing));
    }

    @Transactional(readOnly = true)
    public List<FeedbackResponse> getAllFeedbackResponses() {
        securityAccessUtil.validateAdminLevel(AccessLevel.READ_ONLY, AccessLevel.FULL, AccessLevel.SUPER_ADMIN);

        return feedbackRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public FeedbackResponse updateFeedbackStatus(int id, FeedbackStatus status) {
        securityAccessUtil.validateAdminLevel(AccessLevel.FULL, AccessLevel.SUPER_ADMIN);

        Feedback existing = feedbackRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Feedback not found"));

        existing.setStatus(status);
        return mapToResponse(feedbackRepository.save(existing));
    }

    @Transactional(readOnly = true)
    public FeedbackResponse getFeedbackResponseById(int id) {
        return feedbackRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Feedback not found"));
    }

    @Transactional
    public void removeFeedback(int id) {
        Feedback existing = feedbackRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Feedback not found"));

        securityAccessUtil.validateModificationAccess(existing.getPatient().getUser().getEmail());
        feedbackRepository.delete(existing);
    }

    public List<FeedbackResponse> getFeedbackResponsesByPatient(int patientId) {
        return feedbackRepository.findByPatientPatientId(patientId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Mapping Helper
    private FeedbackResponse mapToResponse(Feedback f) {
        return new FeedbackResponse(
                f.getFeedbackId(),
                f.getAppointment().getAppointmentId(),
                f.getPatient().getPatientId(),
                f.getPatient().getUser().getFirstName() + " " + f.getPatient().getUser().getLastName(),
                f.getDoctor().getDoctorId(),
                "Dr. " + f.getDoctor().getUser().getLastName(),
                f.getRating(),
                f.getComments(),
                f.getStatus(),
                f.getCreatedAt()
        );
    }
}