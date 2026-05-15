package com.medical.appointment.model;

import com.medical.appointment.model.enums.BillingStatus;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "billing")
@Getter
@Setter
@NoArgsConstructor

public class Billing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer billingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", referencedColumnName = "appointmentId", nullable = false)
    @NotNull(message = "Appointment reference is required")
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @NotNull(message = "Patient reference is required")
    private Patient patient;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", message = "Total amount cannot be negative")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @DecimalMin(value = "0.0", message = "Discount cannot be negative")
    @Column(precision = 10, scale = 2)
    private BigDecimal discount;

    @DecimalMin(value = "0.0", message = "Tax cannot be negative")
    @Column(precision = 10, scale = 2)
    private BigDecimal tax;

    @NotNull(message = "Final amount is required")
    @DecimalMin(value = "0.0", message = "Final amount cannot be negative")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal finalAmount;

    @NotNull(message = "Billing date is required")
    @Column(nullable = false)
    private LocalDate billingDate;

    @NotNull(message = "Due date is required")
    @Column(nullable = false)
    private LocalDate dueDate;

    @NotNull(message = "Billing status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BillingStatus status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
