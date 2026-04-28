package com.medical.appointment.dto.admin.response;

import com.medical.appointment.model.enums.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminResponse {
    private Integer userId;
    private String email;
    private String firstName;
    private String lastName;
    private String department;
    private AccessLevel accessLevel;
    private Boolean isActive;
    private String createdAt;
    private String updatedAt;
}