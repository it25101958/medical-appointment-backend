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
}