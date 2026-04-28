package com.medical.appointment.dto.doctor.request;

import com.medical.appointment.dto.auth.request.BaseUserRequest;
import com.medical.appointment.model.enums.Specialization;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DoctorRegisterRequest extends BaseUserRequest {
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotNull(message = "Specialization is required")
    private Specialization specialization;

    @NotBlank(message = "License number is required")
    private String licenseNumber;

    @NotBlank(message = "Qualification is required")
    private String qualification;

    @Min(0)
    private Integer experienceYears;

    @Positive
    private Double consultationFee;
}