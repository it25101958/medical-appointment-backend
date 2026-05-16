package com.medical.appointment.dto.billing.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter

public class BillingRequest {
    private Integer appointmentId;
    private Integer patientId;
    private BigDecimal totalAmount;
    private LocalDate billingDate;
    private LocalDate dueDate;

}
