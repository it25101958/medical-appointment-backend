package com.medical.appointment.controller;

import com.medical.appointment.dto.labtest.request.LabTestRequest;
import com.medical.appointment.dto.labtest.response.LabTestResponse;
import com.medical.appointment.service.LabTestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lab-test")
@RequiredArgsConstructor
public class LabTestController {

    private final LabTestService labTestService;

    @PostMapping
    public ResponseEntity<LabTestResponse> createLabTest(@Valid @RequestBody LabTestRequest request) {
        return ResponseEntity.ok(labTestService.createLabTest(request));
    }

    @GetMapping
    public ResponseEntity<List<LabTestResponse>> getAllLabTests() {
        return ResponseEntity.ok(labTestService.getAllLabTests());
    }

    @GetMapping("/active")
    public ResponseEntity<List<LabTestResponse>> getActiveLabTests() {
        return ResponseEntity.ok(labTestService.getActiveLabTests());
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<LabTestResponse>> getInactiveLabTests() {
        return ResponseEntity.ok(labTestService.getInactiveLabTests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabTestResponse> getLabTestById(@PathVariable Integer id) {
        return ResponseEntity.ok(labTestService.getLabTestById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabTestResponse> updateLabTest(
            @PathVariable Integer id,
            @Valid @RequestBody LabTestRequest request) {
        return ResponseEntity.ok(labTestService.updateLabTest(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLabTest(@PathVariable Integer id) {
        labTestService.deleteLabTest(id);
        return ResponseEntity.noContent().build();
    }
}

