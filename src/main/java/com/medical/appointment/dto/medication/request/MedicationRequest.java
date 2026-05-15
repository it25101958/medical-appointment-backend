package com.medical.appointment.dto.medication.request;

import com.medical.appointment.model.enums.MedicationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MedicationRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Generic name is required")
    private String genericName;

    @NotBlank(message = "Manufacturer is required")
    private String manufacturer;

    @NotBlank(message = "Dosage is required")
    private String dosage;

    @NotBlank(message = "Dosage form is required")
    private String dosageForm;

    @NotNull(message = "Status is required")
    private MedicationStatus status;
}