package com.medical.appointment.controller;

import com.medical.appointment.model.Billing;
import com.medical.appointment.service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/billing")
@CrossOrigin(origins = "*")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @PostMapping("/create")
    public ResponseEntity<Billing> createBilling(@RequestBody Billing billing) {
        return ResponseEntity.ok(billingService.createBilling(billing));
    }

    @GetMapping
    public ResponseEntity<List<Billing>> getAllBillings() {
        return ResponseEntity.ok(billingService.getAllBillings());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Billing> getBillingById(@PathVariable Integer id) {
        return billingService.getBillingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Billing>> getBillingsByPatient(
            @PathVariable Integer patientId) {
        return ResponseEntity.ok(billingService.getBillingsByPatient(patientId));
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<List<Billing>> getBillingsByAppointment(
            @PathVariable Integer appointmentId) {
        return ResponseEntity.ok(billingService.getBillingsByAppointment(appointmentId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Billing> updateBilling(
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