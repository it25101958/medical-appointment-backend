package com.medical.appointment.controller;

import com.medical.appointment.dto.billing.request.BillingRequest;
import com.medical.appointment.dto.billing.response.BillingResponse;
import com.medical.appointment.model.Billing;
import com.medical.appointment.service.BillingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/billing")
@RequiredArgsConstructor

public class BillingController {

    private final BillingService billingService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<BillingResponse> createBilling(
            @Valid @RequestBody BillingRequest request) {
        return new ResponseEntity<>(billingService.createBilling(request), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<BillingResponse>> getAllBillings() {
        return ResponseEntity.ok(billingService.getAllBillings());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'PATIENT')")
    public ResponseEntity<BillingResponse> getBillingById(@PathVariable Integer id) {
        return ResponseEntity.ok(billingService.getBillingById(id));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'PATIENT')")
    public ResponseEntity<List<BillingResponse>> getBillingsByPatient(
            @PathVariable Integer patientId) {
        return ResponseEntity.ok(billingService.getBillingsByPatient(patientId));
    }

    @GetMapping("/appointment/{appointmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'PATIENT')")
    public ResponseEntity<List<BillingResponse>> getBillingsByAppointment(
            @PathVariable Integer appointmentId) {
        return ResponseEntity.ok(billingService.getBillingsByAppointment(appointmentId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<BillingResponse> updateBilling(
            @PathVariable Integer id,
            @Valid @RequestBody BillingRequest request) {
        return ResponseEntity.ok(billingService.updateBilling(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> deleteBilling(@PathVariable Integer id) {
        billingService.deleteBilling(id);
        return ResponseEntity.noContent().build();
    }
}