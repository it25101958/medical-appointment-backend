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
@RequestMapping("/api/feedbacks")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FeedbackController {

    private final FeedbackService feedbackService;


    // Create a new feedback
    @PostMapping
    public ResponseEntity<Feedback> createFeedback(@Valid @RequestBody Feedback feedback) {
        Feedback created = feedbackService.createFeedback(feedback);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Get all feedbacks
    @GetMapping
    public ResponseEntity<List<Feedback>> getAllFeedbacks() {
        return ResponseEntity.ok(feedbackService.getAllFeedbacks());
    }

    // Get one feedback by its ID
    @GetMapping("/{id}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable int id) {
        return ResponseEntity.ok(feedbackService.getFeedbackById(id));
    }

    // Get single feedback by doctor
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<Feedback> getFeedbackByDoctor(@PathVariable int doctorId) {
        return ResponseEntity.ok(feedbackService.getFeedbackByDoctor(doctorId));
    }

    // Get all feedbacks for a doctor
    @GetMapping("/doctor/{doctorId}/all")
    public ResponseEntity<List<Feedback>> getAllFeedbacksByDoctor(@PathVariable int doctorId) {
        return ResponseEntity.ok(feedbackService.getFeedbacksByDoctor(doctorId));
    }

    // Get single feedback by patient
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Feedback> getFeedbackByPatient(@PathVariable int patientId) {
        return ResponseEntity.ok(feedbackService.getFeedbackByPatient(patientId));
    }

    // Get all feedbacks for a patient
    @GetMapping("/patient/{patientId}/all")
    public ResponseEntity<List<Feedback>> getAllFeedbacksByPatient(@PathVariable int patientId) {
        return ResponseEntity.ok(feedbackService.getFeedbacksByPatient(patientId));
    }

    // Get all feedbacks for an appointment
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<List<Feedback>> getFeedbacksByAppointment(
            @PathVariable int appointmentId) {
        return ResponseEntity.ok(feedbackService.getFeedbacksByAppointment(appointmentId));
    }

    // Get feedbacks filtered by status(pending, approved, rejected, archived)
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Feedback>> getFeedbacksByStatus(
            @PathVariable FeedbackStatus status) {
        return ResponseEntity.ok(feedbackService.getFeedbacksByStatus(status));
    }

    // Update rating and comments of a feedback
    @PutMapping("/{id}")
    public ResponseEntity<Feedback> updateFeedback(
            @PathVariable int id,
            @Valid @RequestBody Feedback feedback) {
        return ResponseEntity.ok(feedbackService.updateFeedback(id, feedback));
    }

    // Update only the status (admin use: approve / reject)
    @PatchMapping("/{id}/status")
    public ResponseEntity<Feedback> updateFeedbackStatus(
            @PathVariable int id,
            @RequestParam FeedbackStatus status) {
        return ResponseEntity.ok(feedbackService.updateFeedbackStatus(id, status));
    }

    // Delete a feedback
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable int id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }
}