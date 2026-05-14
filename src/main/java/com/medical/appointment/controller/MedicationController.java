package com.medical.appointment.controller;

import com.medical.appointment.model.Medication;
import com.medical.appointment.model.enums.MedicationStatus;
import com.medical.appointment.service.MedicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medications")
@RequiredArgsConstructor

public class MedicationController {
    private final MedicationService medicationService;

    // To allow only Admins and Doctors to access the createMedication() endpoint
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @PostMapping
    public ResponseEntity<Medication> createMedication(
            @Valid @RequestBody Medication medication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(medicationService.createMedication(medication));
    }

    // Get all medications (get/api/medications)
    @GetMapping
    public ResponseEntity<List<Medication>> getAllMedications() {
        return ResponseEntity.ok(medicationService.getAllMedications());
    }

    // Get one medication by ID(GET/api/medications/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Medication> getMedicationById(@PathVariable int id) {
        return ResponseEntity.ok(medicationService.getMedicationById(id));
    }

    // Get medication by exact name(GET/api/medications/name/{name}
    @GetMapping("/name/{name}")
    public ResponseEntity<Medication> getMedicationByName(@PathVariable String name) {
        return ResponseEntity.ok(medicationService.getMedicationByName(name));
    }

    // Search medications by partial name (GET/api/medications/search?name)
    @GetMapping("/search")
    public ResponseEntity<List<Medication>> searchMedication(@RequestParam String name) {
        return ResponseEntity.ok(medicationService.searchMedication(name));
    }

    // Search medications by generic name(GET/api?medications/search/generic?name)
    @GetMapping("/search/generic")
    public ResponseEntity<List<Medication>> searchByGenericName(@RequestParam String name) {
        return ResponseEntity.ok(medicationService.searchByGenericName(name));
    }

    // Filter by status(GET/api/medications/status/{status}
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Medication>> getMedicationsByStatus(
            @PathVariable MedicationStatus status) {
        return ResponseEntity.ok(medicationService.getMedicationsByStatus(status));
    }

    // Filter by dosage form (GET/apimedications/dosage-form/{form}
    @GetMapping("/dosage-form/{form}")
    public ResponseEntity<List<Medication>> getMedicationsByDosageForm(
            @PathVariable String form) {
        return ResponseEntity.ok(medicationService.getMedicationsByDosageForm(form));
    }

    // Filter by manufacturer(GET/api/medications/manufacture?name = Appitculture)
    @GetMapping("/manufacturer")
    public ResponseEntity<List<Medication>> getMedicationsByManufacturer(
            @RequestParam String name) {
        return ResponseEntity.ok(medicationService.getMedicationsByManufacturer(name));
    }

    // Update medication details(PUT/api/medications/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Medication> updateMedication(
            @PathVariable int id,
            @Valid @RequestBody Medication medication) {
        return ResponseEntity.ok(medicationService.updateMedication(id, medication));
    }

    // Update only the status (PATCH/api/medications/{id}/status)
    @PatchMapping("/{id}/status")
    public ResponseEntity<Medication> updateMedicationStatus(
            @PathVariable int id,
            @RequestParam MedicationStatus status) {
        return ResponseEntity.ok(medicationService.updateMedicationStatus(id, status));
    }

    // Delete a medication(DELETE/api/medications/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable int id) {
        medicationService.deleteMedication(id);
        return ResponseEntity.noContent().build();
    }
}