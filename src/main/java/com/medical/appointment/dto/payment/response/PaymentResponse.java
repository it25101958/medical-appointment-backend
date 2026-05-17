package com.medical.appointment.dto.payment.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class PaymentResponse {

    private Integer paymentId;
    private Integer patientId;
    private Integer appointmentId;
    private Double amount;

    private Integer paymentMethod;
    private Integer paymentStatus;
    private String transactionId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
