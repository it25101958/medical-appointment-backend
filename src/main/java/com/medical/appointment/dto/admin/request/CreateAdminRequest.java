package com.medical.appointment.dto.admin.request;

import com.medical.appointment.dto.auth.request.BaseUserRequest;
import com.medical.appointment.model.enums.AccessLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateAdminRequest extends BaseUserRequest {
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;


    @NotBlank(message = "Department is required")
    private String department;

    @NotNull(message = "Access level is required")
    private AccessLevel accessLevel;
}