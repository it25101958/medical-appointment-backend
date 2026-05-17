package com.medical.appointment.controller;

import com.medical.appointment.dto.labresult.request.LabResultRequest;
import com.medical.appointment.dto.labresult.response.LabResultResponse;
import com.medical.appointment.service.LabResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lab-results")
@RequiredArgsConstructor
public class LabResultController {

    private final LabResultService labResultService;

    @PostMapping
    public ResponseEntity<LabResultResponse> createLabResult(@RequestBody LabResultRequest request) {
        return new ResponseEntity<>(labResultService.createLabResult(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabResultResponse> getLabResultById(@PathVariable Long id) {
        return ResponseEntity.ok(labResultService.getLabResultById(id));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<LabResultResponse>> getLabResultsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(labResultService.getLabResultsByPatientId(patientId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabResultResponse> updateLabResult(
            @PathVariable Long id,
            @RequestBody LabResultRequest request) {
        return ResponseEntity.ok(labResultService.updateLabResult(id, request));
    }
}