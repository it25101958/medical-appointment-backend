package com.medical.appointment.service;

import com.medical.appointment.dto.payment.request.PaymentRequest;
import com.medical.appointment.dto.payment.response.PaymentResponse;
import com.medical.appointment.model.Appointment;
import com.medical.appointment.model.Billing;
import com.medical.appointment.model.Patient;
import com.medical.appointment.model.Payment;
import com.medical.appointment.model.enums.AppointmentStatus;
import com.medical.appointment.model.enums.BillingStatus;
import com.medical.appointment.repository.AppointmentRepository;
import com.medical.appointment.repository.BillingRepository;
import com.medical.appointment.repository.PatientRepository;
import com.medical.appointment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BillingRepository billingRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;


    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {

        // Find patient
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException(
                        "Patient not found with id: " + request.getPatientId()));

        // Find appointment
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new RuntimeException(
                        "Appointment not found with id: " + request.getAppointmentId()));

        // Build payment from request
        Payment payment = new Payment();
        payment.setPatient(patient);
        payment.setAppointment(appointment);
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaymentStatus(request.getPaymentStatus());
        payment.setTransactionId(request.getTransactionId());

        Payment saved = paymentRepository.save(payment);


        List<Billing> billings = billingRepository
                .findByAppointment_AppointmentId(request.getAppointmentId());
        for (Billing billing : billings) {
            billing.setStatus(BillingStatus.PAID);
            billingRepository.save(billing);
        }

        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointmentRepository.save(appointment);

        return convertToResponse(saved);
    }


    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(Integer id) {
        return paymentRepository.findById(id)
                .map(this::convertToResponse)
                .orElseThrow(() -> new RuntimeException(
                        "Payment not found with id: " + id));
    }


    @Transactional(readOnly = true)
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByPatient(Integer patientId) {
        return paymentRepository.findByPatient_PatientId(patientId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByAppointment(Integer appointmentId) {
        return paymentRepository.findByAppointment_AppointmentId(appointmentId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByStatus(Integer paymentStatus) {
        return paymentRepository.findByPaymentStatus(paymentStatus).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByTransactionId(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId);
        if (payment == null) {
            throw new RuntimeException(
                    "Payment not found with transactionId: " + transactionId);
        }
        return convertToResponse(payment);
    }


    @Transactional
    public PaymentResponse updatePayment(Integer id, PaymentRequest request) {
        return paymentRepository.findById(id).map(existing -> {
            existing.setAmount(request.getAmount());
            existing.setPaymentMethod(request.getPaymentMethod());
            existing.setPaymentStatus(request.getPaymentStatus());
            existing.setTransactionId(request.getTransactionId());
            return convertToResponse(paymentRepository.save(existing));
        }).orElseThrow(() -> new RuntimeException(
                "Payment not found with id: " + id));
    }


    @Transactional
    public void deletePayment(Integer id) {
        if (!paymentRepository.existsById(id)) {
            throw new RuntimeException(
                    "Cannot delete: Payment not found with id: " + id);
        }
        paymentRepository.deleteById(id);
    }


    private PaymentResponse convertToResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(payment.getPaymentId());
        response.setAmount(payment.getAmount());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setPaymentStatus(payment.getPaymentStatus());
        response.setTransactionId(payment.getTransactionId());
        response.setCreatedAt(payment.getCreatedAt());
        response.setUpdatedAt(payment.getUpdatedAt());
        if (payment.getPatient() != null) {
            response.setPatientId(payment.getPatient().getPatientId());
        }
        if (payment.getAppointment() != null) {
            response.setAppointmentId(payment.getAppointment().getAppointmentId());
        }
        return response;
    }
}