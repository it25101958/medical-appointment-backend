package com.medical.appointment.controller;

import com.medical.appointment.dto.prescriptionItem.request.PrescriptionItemRequest;
import com.medical.appointment.dto.prescriptionItem.response.PrescriptionItemResponse;
import com.medical.appointment.service.PrescriptionItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
    public ResponseEntity<Page<PrescriptionItemResponse>> getItemsByPrescription(
            @PathVariable Integer prescriptionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.ASC, "prescriptionItemId")
        );

        return ResponseEntity.ok(
                prescriptionItemService.getItemsByPrescriptionId(prescriptionId, pageable)
        );
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
