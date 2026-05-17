package com.medical.appointment.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Entity
@DiscriminatorValue("CONSULTATION")
@Getter
@Setter
@NoArgsConstructor
public class ConsultationBillingItem extends BillingItem {

    @Override
    public BigDecimal calculateTotalPrice() {
        BigDecimal base = super.calculateTotalPrice();
        BigDecimal surcharge = base.multiply(new BigDecimal("0.10"));
        return base.add(surcharge);
    }

}