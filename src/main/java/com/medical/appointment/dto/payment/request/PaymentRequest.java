package com.medical.appointment.dto.payment.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    private Integer patientId;
    private Integer appointmentId;
    private Double amount;
    private Integer paymentMethod;
    private Integer paymentStatus;
    private String transactionId;
}
