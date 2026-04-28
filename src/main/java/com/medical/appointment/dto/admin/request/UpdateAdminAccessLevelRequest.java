package com.medical.appointment.dto.admin.request;

import com.medical.appointment.model.enums.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAdminAccessLevelRequest {
    private AccessLevel accessLevel;
}