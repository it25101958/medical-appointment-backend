package com.medical.appointment.model;

import jakarta.persistence.*;
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

}