package com.medical.appointment.controller;

import com.medical.appointment.model.Billing;
import com.medical.appointment.service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billing")
@CrossOrigin(origins = "*")
public class BillingController {

    @Autowired
    private BillingService billingService;

    // POST /api/billing → Create a new bill
    @PostMapping
    public ResponseEntity<Billing> createBilling(@RequestBody Billing billing) {
        return ResponseEntity.ok(billingService.createBilling(billing));
    }

    // GET /api/billing → Get all bills
    @GetMapping
    public ResponseEntity<List<Billing>> getAllBillings() {
        return ResponseEntity.ok(billingService.getAllBillings());
    }

    // GET /api/billing/5 → Get bill with ID 5
    @GetMapping("/{id}")
    public ResponseEntity<Billing> getBillingById(@PathVariable Integer id) {
        return billingService.getBillingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/billing/patient/3 → Get all bills for patient with ID 3
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Billing>> getBillingsByPatient(
            @PathVariable Integer patientId) {
        return ResponseEntity.ok(billingService.getBillingsByPatient(patientId));
    }

    // GET /api/billing/appointment/7 → Get all bills for appointment with ID 7
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<List<Billing>> getBillingsByAppointment(
            @PathVariable Integer appointmentId) {
        return ResponseEntity.ok(billingService.getBillingsByAppointment(appointmentId));
    }

    // PUT /api/billing/5 → Update bill with ID 5
    @PutMapping("/{id}")
    public ResponseEntity<Billing> updateBilling(
            @PathVariable Integer id,
            @RequestBody Billing billing) {
        return ResponseEntity.ok(billingService.updateBilling(id, billing));
    }

    // DELETE /api/billing/5 → Delete bill with ID 5
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBilling(@PathVariable Integer id) {
        billingService.deleteBilling(id);
        return ResponseEntity.noContent().build();
    }
}