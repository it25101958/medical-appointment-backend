package com.medical.appointment.controller;

import com.medical.appointment.service.LabOrderItemService;
import com.medical.appointment.service.LabOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/lab-order-item")
@RequiredArgsConstructor
public class LabOrderItemController {

    private final LabOrderService labOrderService;
    private final LabOrderItemService labOrderItemService;

    @PatchMapping("/{itemId}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Integer itemId,
            @RequestParam String status) {
        labOrderItemService.updateItemStatus(itemId, status);
        return ResponseEntity.noContent().build();
    }
}
