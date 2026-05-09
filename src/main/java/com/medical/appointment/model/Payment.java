package com.medical.appointment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor

public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;


    @NotNull
    @DecimalMin("0.0")
    @Column(nullable = false)
    private Double amount;


    @NotNull
    @Column(name = "payment_method", nullable = false)
    private Integer paymentMethod;


    @NotNull
    @Column(name = "payment_status", nullable = false)
    private Integer paymentStatus;


    @NotNull
    @Size(max = 255)
    @Column(name = "transaction_id", nullable = false, length = 255)
    private String transactionId;

}