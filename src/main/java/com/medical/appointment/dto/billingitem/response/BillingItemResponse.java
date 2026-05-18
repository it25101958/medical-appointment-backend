package com.medical.appointment.dto.billingitem.response;

import com.medical.appointment.model.enums.BillingItemType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BillingItemResponse {

    private Integer billingItemId;
    private Integer billingId;
    private BillingItemType itemType;
    private String description;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    private Integer medicationId;
    private Integer labTestId;

}