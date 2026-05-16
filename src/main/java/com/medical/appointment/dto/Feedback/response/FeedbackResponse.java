package com.medical.appointment.dto.feedback.response;

import com.medical.appointment.model.enums.FeedbackStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class FeedbackResponse {
    private int feedbackId;
    private int appointmentId;
    private int patientId;
    private String patientName;
    private int doctorId;
    private String doctorName;
    private int rating;
    private String comments;
    private FeedbackStatus status;
    private LocalDateTime createdAt;
}