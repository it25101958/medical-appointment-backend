package com.medical.appointment.dto.labtest.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class LabTestRequest {

    @NotBlank(message = "Test name is required")
    @Size(max = 100, message = "Test name must not exceed 100 characters")
    private String testName;

    @NotBlank(message = "Category is required")
    @Size(max = 50, message = "Category must not exceed 50 characters")
    private String category;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    @NotNull(message = "Standard price is required")
    @Digits(integer = 8, fraction = 2, message = "Price must be a valid decimal with up to 8 digits and 2 decimal places")
    private BigDecimal standardPrice;

    @NotNull(message = "Active status must be specified")
    private Boolean isActive;
}

