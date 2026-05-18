package com.medical.appointment.controller;

import com.medical.appointment.dto.billing.response.BillingResponse;
import com.medical.appointment.model.Billing;
import com.medical.appointment.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/billing")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    @PostMapping
    public ResponseEntity<BillingResponse> createBilling(@RequestBody Billing billing) {
        return new ResponseEntity<>(billingService.createBilling(billing), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BillingResponse>> getAllBillings() {
        return ResponseEntity.ok(billingService.getAllBillings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillingResponse> getBillingById(@PathVariable Integer id) {
        return ResponseEntity.ok(billingService.getBillingById(id));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<BillingResponse>> getBillingsByPatient(@PathVariable Integer patientId) {
        return ResponseEntity.ok(billingService.getBillingsByPatient(patientId));
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<List<BillingResponse>> getBillingsByAppointment(@PathVariable Integer appointmentId) {
        return ResponseEntity.ok(billingService.getBillingsByAppointment(appointmentId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BillingResponse> updateBilling(
            @PathVariable Integer id,
            @RequestBody Billing billing) {
        return ResponseEntity.ok(billingService.updateBilling(id, billing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBilling(@PathVariable Integer id) {
        billingService.deleteBilling(id);
        return ResponseEntity.noContent().build();
    }
}