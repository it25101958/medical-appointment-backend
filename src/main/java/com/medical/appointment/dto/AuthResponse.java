package com.medical.appointment.dto;

import com.medical.appointment.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String email;
    private String firstName;
    private String lastName;
    private int roleType;

    public AuthResponse(String token, User user) {
        this.token = token;
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.roleType = user.getRoleType();
    }
}