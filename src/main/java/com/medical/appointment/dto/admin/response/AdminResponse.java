package com.medical.appointment.dto.admin.response;

import com.medical.appointment.dto.user.response.UserResponse;
import com.medical.appointment.model.enums.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminResponse extends UserResponse {
    private Integer adminId;
    private String department;
    private AccessLevel accessLevel;
}