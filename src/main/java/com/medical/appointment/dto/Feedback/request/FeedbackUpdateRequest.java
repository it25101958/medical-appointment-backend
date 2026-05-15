package com.medical.appointment.dto.feedback.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FeedbackUpdateRequest {
    @Min(1) @Max(5)
    private int rating;

    @Size(max = 255)
    private String comments;
}
