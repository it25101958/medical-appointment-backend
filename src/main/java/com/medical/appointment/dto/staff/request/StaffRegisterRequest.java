package com.medical.appointment.dto.staff.request;

import com.medical.appointment.dto.auth.request.BaseUserRequest;
import com.medical.appointment.model.enums.StaffStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import com.medical.appointment.util.PasswordPolicy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Getter
@Data
@EqualsAndHashCode(callSuper = true)
public class StaffRegisterRequest extends BaseUserRequest {
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters")
    @Pattern(
            regexp = PasswordPolicy.STRONG_PASSWORD_PATTERN,
            message = PasswordPolicy.STRONG_PASSWORD_MESSAGE
    )
    private String password;

    @NotNull(message = "Staff status is required")
    private StaffStatus status;

    @NotBlank(message = "Working hours description is required")
    @Size(max = 100, message = "Working hours text is too long")
    private String workingHours;

    @Size(max = 100, message = "Specialization text is too long")
    private String specialization;
}