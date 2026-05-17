package com.medical.appointment.controller;

import com.medical.appointment.dto.payment.request.PaymentRequest;
import com.medical.appointment.dto.payment.response.PaymentResponse;
import com.medical.appointment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;


    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'PATIENT')")
    public ResponseEntity<PaymentResponse> createPayment(
            @Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.createPayment(request));
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'PATIENT')")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Integer id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }


    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'PATIENT')")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByPatient(
            @PathVariable Integer patientId) {
        return ResponseEntity.ok(paymentService.getPaymentsByPatient(patientId));
    }


    @GetMapping("/appointment/{appointmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'PATIENT')")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByAppointment(
            @PathVariable Integer appointmentId) {
        return ResponseEntity.ok(paymentService.getPaymentsByAppointment(appointmentId));
    }


    @GetMapping("/status/{paymentStatus}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByStatus(
            @PathVariable Integer paymentStatus) {
        return ResponseEntity.ok(paymentService.getPaymentsByStatus(paymentStatus));
    }


    @GetMapping("/transaction/{transactionId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<PaymentResponse> getPaymentByTransactionId(
            @PathVariable String transactionId) {
        return ResponseEntity.ok(
                paymentService.getPaymentByTransactionId(transactionId));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<PaymentResponse> updatePayment(
            @PathVariable Integer id,
            @Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.updatePayment(id, request));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> deletePayment(@PathVariable Integer id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}