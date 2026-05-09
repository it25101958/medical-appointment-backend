package com.medical.appointment.repository;

import com.medical.appointment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {


    List<Payment> findByPatient_PatientId(Integer patientId);


    List<Payment> findByAppointment_AppointmentId(Integer appointmentId);


    List<Payment> findByPaymentStatus(Integer paymentStatus);


    Payment findByTransactionId(String transactionId);
}