package com.medical.appointment.dto.admin.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeUserRoleRequest {
    private Integer roleType;
}