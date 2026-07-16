package com.medical.appointment.service;

import com.medical.appointment.dto.feedback.request.FeedbackRequest;
import com.medical.appointment.dto.feedback.request.FeedbackUpdateRequest;
import com.medical.appointment.dto.feedback.response.FeedbackResponse;
import com.medical.appointment.model.Appointment;
import com.medical.appointment.model.Feedback;
import com.medical.appointment.model.enums.AccessLevel;
import com.medical.appointment.model.enums.FeedbackStatus;
import com.medical.appointment.repository.AppointmentRepository;
import com.medical.appointment.repository.FeedbackRepository;
import com.medical.appointment.security.SecurityAccessUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final AppointmentRepository appointmentRepository;
    private final SecurityAccessUtil securityAccessUtil;

    @Transactional
    public FeedbackResponse createFeedback(FeedbackRequest request) {
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        // Only the patient who owns this appointment can create feedback
        securityAccessUtil.validateStrictOwnership(
                appointment.getPatient().getUser().getEmail()
        );

        // One feedback per appointment
        if (feedbackRepository.existsByAppointmentAppointmentId(request.getAppointmentId())) {
            throw new IllegalStateException("Feedback already exists for this appointment.");
        }

        Feedback feedback = new Feedback();
        feedback.setAppointment(appointment);
        feedback.setPatient(appointment.getPatient());
        feedback.setDoctor(appointment.getDoctor());
        feedback.setRating(request.getRating());
        feedback.setComments(request.getComments());
        feedback.setStatus(FeedbackStatus.PENDING);

        return mapToResponse(feedbackRepository.save(feedback));
    }

    @Transactional
    public FeedbackResponse updateFeedback(int id, FeedbackUpdateRequest request) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Feedback not found"));

        // Only the appointment owner patient can update
        securityAccessUtil.validateStrictOwnership(
                feedback.getAppointment().getPatient().getUser().getEmail()
        );

        if (feedback.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(20))) {
            throw new IllegalStateException("Edit window expired. Feedback can only be edited within 20 minutes.");
        }

        feedback.setRating(request.getRating());
        feedback.setComments(request.getComments());

        return mapToResponse(feedbackRepository.save(feedback));
    }

    @Transactional
    public void removeFeedback(int id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Feedback not found"));

        // Only the appointment owner patient can delete
        securityAccessUtil.validateStrictOwnership(
                feedback.getAppointment().getPatient().getUser().getEmail()
        );

        feedbackRepository.delete(feedback);
    }

    @Transactional(readOnly = true)
    public FeedbackResponse getFeedbackResponseById(int id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Feedback not found"));

        validateFeedbackViewAccess(feedback);

        return mapToResponse(feedback);
    }

    @Transactional(readOnly = true)
    public List<FeedbackResponse> getFeedbackResponsesByAppointment(Integer appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        validateAppointmentFeedbackViewAccess(appointment);

        return feedbackRepository.findByAppointmentAppointmentId(appointmentId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FeedbackResponse> getAllFeedbackResponses() {
        securityAccessUtil.validateAdminLevel(
                AccessLevel.READ_ONLY,
                AccessLevel.FULL,
                AccessLevel.SUPER_ADMIN
        );

        return feedbackRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FeedbackResponse> getPublicFeedbackResponses() {
        return feedbackRepository.findByStatus(FeedbackStatus.APPROVED)
                .stream()
                .sorted(Comparator.comparing(Feedback::getCreatedAt).reversed())
                .limit(8)
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public FeedbackResponse updateFeedbackStatus(int id, FeedbackStatus status) {
        securityAccessUtil.validateAdminLevel(
                AccessLevel.FULL,
                AccessLevel.SUPER_ADMIN
        );

        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Feedback not found"));

        feedback.setStatus(status);

        return mapToResponse(feedbackRepository.save(feedback));
    }

    @Transactional(readOnly = true)
    public List<FeedbackResponse> getFeedbackResponsesByPatient(int patientId) {
        return feedbackRepository.findByPatientPatientId(patientId)
                .stream()
                .filter(this::canCurrentUserViewFeedback)
                .map(this::mapToResponse)
                .toList();
    }

    private void validateFeedbackViewAccess(Feedback feedback) {
        validateAppointmentFeedbackViewAccess(feedback.getAppointment());
    }

    private void validateAppointmentFeedbackViewAccess(Appointment appointment) {
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

        if (!(isPatientOwner || isAssignedDoctor || isAdmin)) {
            throw new AccessDeniedException(
                    "Access Denied: You do not have permission to view feedback for this appointment."
            );
        }
    }

    private boolean canCurrentUserViewFeedback(Feedback feedback) {
        try {
            validateFeedbackViewAccess(feedback);
            return true;
        } catch (AccessDeniedException ex) {
            return false;
        }
    }

    private FeedbackResponse mapToResponse(Feedback feedback) {
        return new FeedbackResponse(
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
