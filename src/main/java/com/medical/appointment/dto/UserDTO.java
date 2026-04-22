package com.medical.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Integer userId;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String NIC;
    private String address;
    private Boolean isActive;
    private Integer roleType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}