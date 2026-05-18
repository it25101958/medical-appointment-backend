package com.medical.appointment.dto.billing.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter

public class BillingRequest {
    @NotNull(message = "Appointment ID is required")
    private Integer appointmentId;

    @NotNull(message = "Patient ID is required")
    private Integer patientId;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", message = "Total amount must be positive")
    private BigDecimal totalAmount;

    @DecimalMin(value = "0.0", message = "Discount must be positive")
    private BigDecimal discount;

    @DecimalMin(value = "0.0", message = "Tax must be positive")
    private BigDecimal tax;

    @NotNull(message = "Billing date is required")
    private LocalDate billingDate;

    @NotNull(message = "Due date is required")
    @FutureOrPresent(message = "Due date must be today or in the future")
    private LocalDate dueDate;

}
