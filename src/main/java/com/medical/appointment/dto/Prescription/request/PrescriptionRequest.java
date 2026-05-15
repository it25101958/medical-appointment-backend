package com.medical.appointment.dto.Prescription.request;

import com.medical.appointment.model.enums.PrescriptionStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

import com.medical.appointment.dto.prescriptionItem.request.PrescriptionItemRequest;
import com.medical.appointment.model.enums.PrescriptionStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PrescriptionRequest {

    @NotNull(message = "Appointment ID is required")
    private Integer appointmentId;

    private String notes;

    @NotNull(message = "Status is required")
    private PrescriptionStatus status;

    @NotEmpty(message = "At least one medication item is required")
    @Valid // This ensures every item in the list is validated
    private List<PrescriptionItemRequest> items;
}