package com.medical.appointment.controller;

import com.medical.appointment.model.Feedback;
import com.medical.appointment.model.FeedbackStatus;
import com.medical.appointment.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @Transactional
    public Feedback createFeedback(Feedback feedback) {
        feedback.setStatus(FeedbackStatus.PENDING);
        return feedbackRepository.save(feedback);
    }

    // get feedback (READ)
    public Feedback getFeedbackById(int feedbackId) {
        return feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Feedback not found with id: " + feedbackId));
    }

    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    public List<Feedback> getFeedbacksByDoctor(int doctorId) {
        return feedbackRepository.findByDoctorDoctorId(doctorId);
    }

    public List<Feedback> getFeedbacksByPatient(int patientId) {
        return feedbackRepository.findByPatientPatientId(patientId);
    }

    public List<Feedback> getFeedbacksByAppointment(int appointmentId) {
        return feedbackRepository.findByAppointmentAppointmentId(appointmentId);
    }

    public List<Feedback> getFeedbacksByStatus(FeedbackStatus status) {
        return feedbackRepository.findByStatus(status);
    }

    //  patents' view approved feedback for a doctor
    public List<Feedback> getApprovedFeedbacksByDoctor(int doctorId) {
        return feedbackRepository.findByDoctorDoctorId(doctorId).stream()
                .filter(f -> f.getStatus() == FeedbackStatus.APPROVED)
                .collect(java.util.stream.Collectors.toList());
    }

    // patients can edit the feed back only within 20 min(UPDATE)
    @Transactional
    public Feedback updateFeedback(int feedbackId, Feedback updatedFeedback) {
        Feedback existing = getFeedbackById(feedbackId);

        // 20 min edit window checking
        if (existing.getCreatedAt().isBefore(
                java.time.LocalDateTime.now().minusMinutes(20))) {
            throw new IllegalStateException(
                    "Feedback can only be edited within 20 minutes of creation.");
        }

        existing.setRating(updatedFeedback.getRating());
        existing.setComments(updatedFeedback.getComments());
        return feedbackRepository.save(existing);
    }

    @Transactional
    public Feedback updateFeedbackStatus(int feedbackId, FeedbackStatus status) {
        Feedback existing = getFeedbackById(feedbackId);
        existing.setStatus(status);
        return feedbackRepository.save(existing);
    }

    // delete feedbacks
    @Transactional
    public void deleteFeedback(int feedbackId) {
        if (!feedbackRepository.existsById(feedbackId)) {
            throw new EntityNotFoundException(
                    "Feedback not found with id: " + feedbackId);
        }
        feedbackRepository.deleteById(feedbackId);
    }

    // find feedbacks
    public Feedback getFeedbackByPatient(int patientId) {
        return feedbackRepository.findFirstByPatientPatientId(patientId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No feedback found for patient id: " + patientId));
    }

    public Feedback getFeedbackByDoctor(int doctorId) {
        return feedbackRepository.findFirstByDoctorDoctorId(doctorId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No feedback found for doctor id: " + doctorId));
    }
}