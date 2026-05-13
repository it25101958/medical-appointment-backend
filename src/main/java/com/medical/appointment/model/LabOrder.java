package com.medical.appointment.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "lab_order")
@Getter
@Setter
@NoArgsConstructor
public class LabOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lab_order_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", referencedColumnName = "appointmentId", nullable = false)
    private Appointment appointment;

}