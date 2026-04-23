package com.medical.appointment.dto.patient.request;

import com.medical.appointment.dto.auth.request.BaseUserRequest;
import com.medical.appointment.model.enums.BloodGroup;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PatientRegisterRequest extends BaseUserRequest {
    @NotBlank(message = "Emergency contact is required")
    private String emergencyContact;

    @NotNull(message = "Blood group is required")
    private BloodGroup bloodGroup;

    private String allergies;
}