package com.medical.appointment.dto.feedback.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FeedbackRequest {
    @NotNull(message = "Appointment ID is required")
    private Integer appointmentId;

    @NotNull(message = "Patient ID is required")
    private Integer patientId;

    @NotNull(message = "Doctor ID is required")
    private Integer doctorId;

    @Min(1) @Max(5)
    private int rating;

    @Size(max = 255)
    private String comments;
}
