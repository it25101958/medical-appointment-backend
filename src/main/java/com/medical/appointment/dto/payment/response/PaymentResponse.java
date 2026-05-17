package com.medical.appointment.dto.payment.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PaymentResponse {
    private Integer paymentId;
    private Integer patientId;
    private Integer appointmentId;
    private Double amount;
}
