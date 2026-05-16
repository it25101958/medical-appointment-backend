package com.medical.appointment.dto.admin.request;

import com.medical.appointment.dto.auth.request.BaseUserRequest;
import com.medical.appointment.model.enums.AccessLevel;
import com.medical.appointment.util.PasswordPolicy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.Pattern;


@Data
@EqualsAndHashCode(callSuper = true)
public class CreateAdminRequest extends BaseUserRequest {
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters")
    @Pattern(
            regexp = PasswordPolicy.STRONG_PASSWORD_PATTERN,
            message = PasswordPolicy.STRONG_PASSWORD_MESSAGE
    )
    private String password;

    @NotBlank(message = "Department is required")
    private String department;

    @NotNull(message = "Access level is required")
    private AccessLevel accessLevel;
}