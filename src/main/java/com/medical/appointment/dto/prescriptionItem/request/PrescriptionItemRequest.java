package com.medical.appointment.dto.prescriptionItem.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PrescriptionItemRequest {

    @NotNull(message = "Medication ID is required")
    private Integer medicationId;

    @NotBlank(message = "Dosage is required")
    @Size(max = 50, message = "Dosage must not exceed 50 characters")
    private String dosage; //

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity; //

    @Size(max = 255, message = "Instructions must not exceed 255 characters")
    private String specialInstructions; //
}