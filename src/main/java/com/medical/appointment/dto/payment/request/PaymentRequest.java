package com.medical.appointment.dto.payment.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter


public class PaymentRequest {

    @NotNull(message = "Patient ID is required")
    private Integer patientId;

    @NotNull(message = "Appointment ID is required")
    private Integer appointmentId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", message = "Amount must be positive")
    private Double amount;

    @NotNull(message = "Payment method is required")
    private Integer paymentMethod;

    @NotNull(message = "Payment status is required")
    private Integer paymentStatus;

    @NotNull(message = "Transaction ID is required")
    @Size(max = 255, message = "Transaction ID cannot exceed 255 characters")
    private String transactionId;
}
