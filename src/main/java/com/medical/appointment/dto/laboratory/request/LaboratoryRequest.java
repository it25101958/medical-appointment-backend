package com.medical.appointment.dto.laboratory.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LaboratoryRequest {

    @NotBlank(message = "Laboratory name is required")
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Opening hours are required")
    @Pattern(regexp = "^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]\\s?-\\s?([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$",
            message = "Opening hours must be in format HH:mm - HH:mm")
    private String openingHours;

    @NotBlank(message = "Phone number is required")
    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    @Pattern(regexp = "^\\+?[0-9\\-\\s]+$", message = "Invalid phone number format")
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;
}