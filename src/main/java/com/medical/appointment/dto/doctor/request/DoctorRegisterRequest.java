package com.medical.appointment.dto.doctor.request;

import com.medical.appointment.dto.auth.request.BaseUserRequest;
import com.medical.appointment.model.enums.Specialization;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.medical.appointment.util.PasswordPolicy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = true)
public class DoctorRegisterRequest extends BaseUserRequest {
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters")
    @Pattern(
            regexp = PasswordPolicy.STRONG_PASSWORD_PATTERN,
            message = PasswordPolicy.STRONG_PASSWORD_MESSAGE
    )
    private String password;

    @NotNull(message = "Specialization is required")
    private Specialization specialization;

    @NotBlank(message = "License number is required")
    private String licenseNumber;

    @NotBlank(message = "Qualification is required")
    private String qualification;

    @NotNull(message = "Experience years is required")
    @Min(value = 0, message = "Experience years cannot be negative")
    private Integer experienceYears;

    @NotNull(message = "Consultation fee is required")
    @Positive(message = "Consultation fee must be greater than zero")
    private Double consultationFee;
}