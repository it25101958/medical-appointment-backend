package com.medical.appointment.dto;

import com.medical.appointment.model.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserRegistration {

    @NotBlank @Email private String email;
    @NotBlank @Size(min = 8) private String password;
    @NotBlank private String firstName;
    @NotBlank private String lastName;
    @NotBlank private String phone;
    @NotBlank private String NIC;
    @NotNull private LocalDate dateOfBirth;
    @NotNull private Gender gender;
    @NotBlank private String address;
    @NotNull private Integer roleType;
}