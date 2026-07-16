package com.medical.appointment.dto.billing.response;
import com.medical.appointment.model.enums.BillingStatus;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class BillingResponse {
    private Integer billingId;
    private Integer appointmentId;
    private BigDecimal finalAmount;
    private LocalDate billingDate;
    private LocalDate dueDate;
    private BillingStatus status;
}