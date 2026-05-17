package com.medical.appointment.controller;

import com.medical.appointment.dto.feedback.request.FeedbackRequest;
import com.medical.appointment.dto.feedback.request.FeedbackUpdateRequest;
import com.medical.appointment.dto.feedback.response.FeedbackResponse;
import com.medical.appointment.model.enums.FeedbackStatus;
import com.medical.appointment.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<FeedbackResponse> createFeedback(
            @Valid @RequestBody FeedbackRequest request
    ) {
        return ResponseEntity.ok(feedbackService.createFeedback(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeedbackResponse> getFeedbackById(@PathVariable int id) {
        return ResponseEntity.ok(feedbackService.getFeedbackResponseById(id));
    }

    @GetMapping
    public ResponseEntity<List<FeedbackResponse>> getAllFeedbacks() {
        return ResponseEntity.ok(feedbackService.getAllFeedbackResponses());
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<List<FeedbackResponse>> getByAppointment(
            @PathVariable Integer appointmentId
    ) {
        return ResponseEntity.ok(
                feedbackService.getFeedbackResponsesByAppointment(appointmentId)
        );
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<FeedbackResponse>> getByPatient(@PathVariable int patientId) {
        return ResponseEntity.ok(feedbackService.getFeedbackResponsesByPatient(patientId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResponse> updateFeedback(
            @PathVariable int id,
            @Valid @RequestBody FeedbackUpdateRequest request
    ) {
        return ResponseEntity.ok(feedbackService.updateFeedback(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<FeedbackResponse> updateStatus(
            @PathVariable int id,
            @RequestParam FeedbackStatus status
    ) {
        return ResponseEntity.ok(feedbackService.updateFeedbackStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable int id) {
        feedbackService.removeFeedback(id);
        return ResponseEntity.noContent().build();
    }
}