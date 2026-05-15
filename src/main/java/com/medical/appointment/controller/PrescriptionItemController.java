package com.medical.appointment.controller;

import com.medical.appointment.dto.prescriptionItem.request.PrescriptionItemRequest;
import com.medical.appointment.dto.prescriptionItem.response.PrescriptionItemResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.medical.appointment.dto.prescriptionItem.request.PrescriptionItemRequest;
import com.medical.appointment.dto.prescriptionItem.response.PrescriptionItemResponse;
import com.medical.appointment.service.PrescriptionItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/api/v1/prescription-items")
@RequiredArgsConstructor
public class PrescriptionItemController {

    private final PrescriptionItemService prescriptionItemService;

    @PostMapping("/prescription/{prescriptionId}")
    public ResponseEntity<PrescriptionItemResponse> addItem(
            @PathVariable Integer prescriptionId,
            @Valid @RequestBody PrescriptionItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(prescriptionItemService.addItemToPrescription(prescriptionId, request));
    }

    @GetMapping("/prescription/{prescriptionId}")
    public ResponseEntity<List<PrescriptionItemResponse>> getItemsByPrescription(
            @PathVariable Integer prescriptionId) {
        return ResponseEntity.ok(prescriptionItemService.getItemsByPrescriptionId(prescriptionId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrescriptionItemResponse> updateItem(
            @PathVariable Integer id,
            @Valid @RequestBody PrescriptionItemRequest request) {
        return ResponseEntity.ok(prescriptionItemService.updateItem(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Integer id) {
        prescriptionItemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
