package com.medical.appointment.service;

import com.medical.appointment.model.Payment;
import com.medical.appointment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }


    public Optional<Payment> getPaymentById(Integer id) {
        return paymentRepository.findById(id);
    }


    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public List<Payment> getPaymentsByPatient(Integer patientId) {
        return paymentRepository.findByPatient_PatientId(patientId);
    }


    public List<Payment> getPaymentsByAppointment(Integer appointmentId) {
        return paymentRepository.findByAppointment_AppointmentId(appointmentId);
    }


    public List<Payment> getPaymentsByStatus(Integer paymentStatus) {
        return paymentRepository.findByPaymentStatus(paymentStatus);
    }


    public Payment getPaymentByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId);
    }

    public Payment updatePayment(Integer id, Payment updatedPayment) {
        return paymentRepository.findById(id).map(existing -> {
            existing.setAmount(updatedPayment.getAmount());
            existing.setPaymentMethod(updatedPayment.getPaymentMethod());
            existing.setPaymentStatus(updatedPayment.getPaymentStatus());
            existing.setTransactionId(updatedPayment.getTransactionId());
            existing.setPatient(updatedPayment.getPatient());
            existing.setAppointment(updatedPayment.getAppointment());
            return paymentRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
    }

    public void deletePayment(Integer id) {
        paymentRepository.deleteById(id);
    }

}