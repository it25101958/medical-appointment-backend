package com.medical.appointment.service;

import com.medical.appointment.model.Feedback;
import com.medical.appointment.model.enums.FeedbackStatus;
import com.medical.appointment.repository.FeedbackRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    // add or create the feedback
    @Transactional
    public Feedback createFeedback(Feedback feedback) {
        feedback.setStatus(FeedbackStatus.PENDING);
        return feedbackRepository.save(feedback);
    }

    // get the feedback or read the feedback
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

    // update the current feedback
    @Transactional
    public Feedback updateFeedback(int feedbackId, Feedback updatedFeedback) {
        Feedback existing = getFeedbackById(feedbackId);
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

    // delete the current feedback
    @Transactional
    public void removeFeedback(int feedbackId) {
        if (!feedbackRepository.existsById(feedbackId)) {
            throw new EntityNotFoundException(
                    "Feedback not found with id: " + feedbackId);
        }
        feedbackRepository.deleteById(feedbackId);
    }

    // getting feedback from patient
    public Feedback getFeedbackByPatient(int patientId) {
        return feedbackRepository.findByPatientPatientId(patientId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "No feedback found for patient id: " + patientId));
    }

    // geting feedback from doctor
    public Feedback getFeedbackByDoctor(int doctorId) {
        return feedbackRepository.findByDoctorDoctorId(doctorId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "No feedback found for doctor id: " + doctorId));
    }
}