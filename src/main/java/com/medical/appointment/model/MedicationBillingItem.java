package com.medical.appointment.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("MEDICATION")
@Getter
@Setter
@NoArgsConstructor
public class MedicationBillingItem extends BillingItem {

    @Override
    public BigDecimal calculateTotalPrice() {
        BigDecimal base = super.calculateTotalPrice();
        BigDecimal tax = base.multiply(new BigDecimal("0.05"));
        return base.add(tax);
    }

}