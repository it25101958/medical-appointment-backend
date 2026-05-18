package com.medical.appointment.model;

import com.medical.appointment.model.enums.BillingItemType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "billing_item")
@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "item_category", discriminatorType = DiscriminatorType.STRING)
public class BillingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer billingItemId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_id", nullable = false)
    private Billing billing;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_test_id", nullable = true)
    private LabTest labTest;


    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false, length = 50)
    private BillingItemType itemType;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String description;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer quantity;

    @NotNull
    @DecimalMin("0.0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @NotNull
    @DecimalMin("0.0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;


    public BigDecimal calculateTotalPrice() {
        if (unitPrice == null || quantity == null) return BigDecimal.ZERO;
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}