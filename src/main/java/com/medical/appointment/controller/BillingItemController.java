package com.medical.appointment.controller;

import com.medical.appointment.dto.billingitem.request.BillingItemRequest;
import com.medical.appointment.dto.billingitem.response.BillingItemResponse;
import com.medical.appointment.service.BillingItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/billing-items")
@RequiredArgsConstructor
public class BillingItemController {

    private final BillingItemService billingItemService;


    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<BillingItemResponse> createBillingItem(
            @Valid @RequestBody BillingItemRequest request) {
        return ResponseEntity.ok(billingItemService.createBillingItem(request));
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<BillingItemResponse>> getAllBillingItems() {
        return ResponseEntity.ok(billingItemService.getAllBillingItems());
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<BillingItemResponse> getBillingItemById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(billingItemService.getBillingItemById(id));
    }