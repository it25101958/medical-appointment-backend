package com.medical.appointment.dto.patient.request;

import com.medical.appointment.dto.auth.request.BaseUserRequest;
import com.medical.appointment.model.enums.BloodGroup;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.medical.appointment.util.PasswordPolicy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = true)
public class PatientRegisterRequest extends BaseUserRequest {
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters")
    @Pattern(
            regexp = PasswordPolicy.STRONG_PASSWORD_PATTERN,
            message = PasswordPolicy.STRONG_PASSWORD_MESSAGE
    )
    private String password;
    private String emergencyContact;
    private BloodGroup bloodGroup;
    private String allergies;
}