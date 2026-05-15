package com.medical.appointment.controller;

import com.medical.appointment.dto.medication.request.MedicationRequest;
import com.medical.appointment.dto.medication.response.MedicationResponse;
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
@RequestMapping("/api/v1/medication")
@RequiredArgsConstructor
public class MedicationController {
    private final MedicationService medicationService;

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @PostMapping
    public ResponseEntity<MedicationResponse> createMedication(
            @Valid @RequestBody MedicationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(medicationService.createMedication(request));
    }

    @GetMapping
    public ResponseEntity<List<MedicationResponse>> getAllMedications() {
        return ResponseEntity.ok(medicationService.getAllMedications());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicationResponse> getMedicationById(@PathVariable int id) {
        return ResponseEntity.ok(medicationService.getMedicationById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<MedicationResponse> getMedicationByName(@PathVariable String name) {
        return ResponseEntity.ok(medicationService.getMedicationByName(name));
    }

    @GetMapping("/search")
    public ResponseEntity<List<MedicationResponse>> searchMedication(@RequestParam String name) {
        return ResponseEntity.ok(medicationService.searchMedication(name));
    }

    @GetMapping("/search/generic")
    public ResponseEntity<List<MedicationResponse>> searchByGenericName(@RequestParam String name) {
        return ResponseEntity.ok(medicationService.searchByGenericName(name));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<MedicationResponse>> getMedicationsByStatus(
            @PathVariable MedicationStatus status) {
        return ResponseEntity.ok(medicationService.getMedicationsByStatus(status));
    }

    @GetMapping("/dosage-form/{form}")
    public ResponseEntity<List<MedicationResponse>> getMedicationsByDosageForm(
            @PathVariable String form) {
        return ResponseEntity.ok(medicationService.getMedicationsByDosageForm(form));
    }

    @GetMapping("/manufacturer")
    public ResponseEntity<List<MedicationResponse>> getMedicationsByManufacturer(
            @RequestParam String name) {
        return ResponseEntity.ok(medicationService.getMedicationsByManufacturer(name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicationResponse> updateMedication(
            @PathVariable int id,
            @Valid @RequestBody MedicationRequest request) {
        return ResponseEntity.ok(medicationService.updateMedication(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<MedicationResponse> updateMedicationStatus(
            @PathVariable int id,
            @RequestParam MedicationStatus status) {
        return ResponseEntity.ok(medicationService.updateMedicationStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable int id) {
        medicationService.deleteMedication(id);
        return ResponseEntity.noContent().build();
    }
}