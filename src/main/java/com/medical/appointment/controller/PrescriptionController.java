package com.medical.appointment.controller;

import com.medical.appointment.dto.prescription.request.PrescriptionRequest;
import com.medical.appointment.dto.prescription.response.PrescriptionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.medical.appointment.dto.prescription.request.PrescriptionRequest;
import com.medical.appointment.dto.prescription.response.PrescriptionResponse;
import com.medical.appointment.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/api/v1/prescription")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PostMapping
    public ResponseEntity<PrescriptionResponse> create(@Valid @RequestBody PrescriptionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(prescriptionService.createPrescription(request));
    }

    @GetMapping
    public ResponseEntity<List<PrescriptionResponse>> getAllPrescriptions() {
        List<PrescriptionResponse> prescriptions = prescriptionService.getAllPrescriptions();
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionById(id));
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<PrescriptionResponse> getByAppointment(@PathVariable Integer appointmentId) {
        return ResponseEntity.ok(prescriptionService.getByAppointmentId(appointmentId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrescriptionResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody PrescriptionRequest request) {
        return ResponseEntity.ok(prescriptionService.updatePrescription(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.noContent().build();
    }
}

