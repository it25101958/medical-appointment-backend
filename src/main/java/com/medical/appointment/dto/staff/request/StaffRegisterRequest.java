package com.medical.appointment.dto.staff.request;

import com.medical.appointment.dto.auth.request.BaseUserRequest;
import com.medical.appointment.model.enums.StaffStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StaffRegisterRequest extends BaseUserRequest {
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotNull(message = "Staff status is required")
    private StaffStatus status;

    @NotBlank(message = "Working hours description is required")
    @Size(max = 100, message = "Working hours text is too long")
    private String workingHours;

    @Size(max = 100, message = "Specialization text is too long")
    private String specialization;
}