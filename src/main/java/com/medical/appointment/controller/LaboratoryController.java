package com.medical.appointment.controller;

import com.medical.appointment.dto.laboratory.request.LaboratoryRequest;
import com.medical.appointment.dto.laboratory.response.LaboratoryResponse;
import com.medical.appointment.service.LaboratoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/laboratory")
@RequiredArgsConstructor
public class LaboratoryController {

    private final LaboratoryService laboratoryService;

    @PostMapping
    public ResponseEntity<LaboratoryResponse> create(@Valid @RequestBody LaboratoryRequest request) {
        return new ResponseEntity<>(laboratoryService.createLaboratory(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LaboratoryResponse>> getAll() {
        return ResponseEntity.ok(laboratoryService.getAllLaboratories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LaboratoryResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(laboratoryService.getLaboratoryById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LaboratoryResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody LaboratoryRequest request) {
        return ResponseEntity.ok(laboratoryService.updateLaboratory(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        laboratoryService.deleteLaboratory(id);
        return ResponseEntity.noContent().build();
    }
}