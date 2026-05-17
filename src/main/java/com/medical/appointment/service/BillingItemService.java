package com.medical.appointment.service;

import com.medical.appointment.dto.billingitem.request.BillingItemRequest;
import com.medical.appointment.dto.billingitem.response.BillingItemResponse;
import com.medical.appointment.model.Billing;
import com.medical.appointment.model.BillingItem;
import com.medical.appointment.model.ConsultationBillingItem;
import com.medical.appointment.model.MedicationBillingItem;
import com.medical.appointment.model.enums.BillingItemType;
import com.medical.appointment.repository.BillingItemRepository;
import com.medical.appointment.repository.BillingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillingItemService {

    private final BillingItemRepository billingItemRepository;
    private final BillingRepository billingRepository;

    @Transactional
    public BillingItemResponse createBillingItem(BillingItemRequest request) {
        Billing billing = billingRepository.findById(request.getBillingId())
                .orElseThrow(() -> new RuntimeException("Billing not found with id: " + request.getBillingId()));
        BillingItem item = createBillingItemByType(request.getItemType());
        item.setBilling(billing);
        item.setItemType(request.getItemType());
        item.setDescription(request.getDescription());
        item.setQuantity(request.getQuantity());
        item.setUnitPrice(request.getUnitPrice());
        item.setTotalPrice(item.calculateTotalPrice());
        return convertToResponse(billingItemRepository.save(item));
    }

    @Transactional(readOnly = true)
    public BillingItemResponse getBillingItemById(Integer id) {
        return billingItemRepository.findById(id).map(this::convertToResponse)
                .orElseThrow(() -> new RuntimeException("BillingItem not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<BillingItemResponse> getAllBillingItems() {
        return billingItemRepository.findAll().stream()
                .map(this::convertToResponse).collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<BillingItemResponse> getBillingItemsByBillingId(Integer billingId) {
        return billingItemRepository.findByBilling_BillingId(billingId).stream()
                .map(this::convertToResponse).collect(Collectors.toList());
    }


    @Transactional
    public BillingItemResponse updateBillingItem(Integer id, BillingItemRequest request) {
        BillingItem existing = billingItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BillingItem not found with id: " + id));
        existing.setDescription(request.getDescription());
        existing.setQuantity(request.getQuantity());
        existing.setUnitPrice(request.getUnitPrice());
        existing.setItemType(request.getItemType());
        existing.setTotalPrice(existing.calculateTotalPrice());
        return convertToResponse(billingItemRepository.save(existing));
    }


    @Transactional
    public void deleteBillingItem(Integer id) {
        if (!billingItemRepository.existsById(id))
            throw new RuntimeException("Cannot delete: BillingItem not found with id: " + id);
        billingItemRepository.deleteById(id);
    }

    private BillingItem createBillingItemByType(BillingItemType type) {
        return switch (type) {
            case CONSULTATION -> new ConsultationBillingItem();
            case MEDICATION   -> new MedicationBillingItem();
            default           -> new BillingItem();
        };
    }

    private BillingItemResponse convertToResponse(BillingItem item) {
        BillingItemResponse response = new BillingItemResponse();
        response.setBillingItemId(item.getBillingItemId());
        response.setBillingId(item.getBilling().getBillingId());
        response.setItemType(item.getItemType());
        response.setDescription(item.getDescription());
        response.setQuantity(item.getQuantity());
        response.setUnitPrice(item.getUnitPrice());
        response.setTotalPrice(item.getTotalPrice());
        if (item.getMedication() != null) response.setMedicationId(item.getMedication().getMedicationId());
        if (item.getLabTest() != null) response.setLabTestId(item.getLabTest().getLabTestId());
        return response;
    }
}