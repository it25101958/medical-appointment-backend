package com.medical.appointment.dto.user.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryResponse {

    private Integer userId;
    private String email;
    private String firstName;
    private String lastName;
    private Integer roleType;
    private String roleName;
    private String accessLevel;
    private Boolean isActive;
}