package com.medical.appointment.controller;

import com.medical.appointment.dto.laborder.request.LabOrderRequest;
import com.medical.appointment.dto.laborder.response.LabOrderResponse;
import com.medical.appointment.service.LabOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/lab-order")
@RequiredArgsConstructor
public class LabOrderController {

    private final LabOrderService labOrderService;

    @PostMapping
    public ResponseEntity<LabOrderResponse> create(@Valid @RequestBody LabOrderRequest request) {
        return new ResponseEntity<>(labOrderService.createLabOrder(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabOrderResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(labOrderService.getLabOrderById(id));
    }
}
