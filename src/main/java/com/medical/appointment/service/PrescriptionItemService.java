package com.medical.appointment.service;

import com.medical.appointment.dto.prescriptionItem.request.PrescriptionItemRequest;
import com.medical.appointment.dto.prescriptionItem.response.PrescriptionItemResponse;
import com.medical.appointment.model.Medication;
import com.medical.appointment.model.Prescription;
import com.medical.appointment.model.PrescriptionItem;
import com.medical.appointment.repository.MedicationRepository;
import com.medical.appointment.repository.PrescriptionItemRepository;
import com.medical.appointment.repository.PrescriptionRepository;
import com.medical.appointment.security.SecurityAccessUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PrescriptionItemService {

    private final PrescriptionItemRepository itemRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final MedicationRepository medicationRepository;
    private final SecurityAccessUtil securityAccessUtil;

    @Transactional
    public PrescriptionItemResponse addItemToPrescription(Integer prescriptionId, PrescriptionItemRequest request) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new EntityNotFoundException("Prescription not found with ID: " + prescriptionId));

        securityAccessUtil.validatePrescriptionOwnerAccess(
                prescription.getDoctor().getUser().getEmail()
        );


        Medication medication = medicationRepository.findById(request.getMedicationId())
                .orElseThrow(() -> new EntityNotFoundException("Medication not found with ID: " + request.getMedicationId()));

        PrescriptionItem item = new PrescriptionItem();
        item.setPrescription(prescription);
        item.setMedication(medication);
        item.setDosage(request.getDosage());
        item.setQuantity(request.getQuantity());
        item.setSpecialInstructions(request.getSpecialInstructions());

        return mapToResponse(itemRepository.save(item));
    }

    @Transactional(readOnly = true)
    public Page<PrescriptionItemResponse> getItemsByPrescriptionId(Integer prescriptionId, Pageable pageable) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new EntityNotFoundException("Prescription not found with ID: " + prescriptionId));

        securityAccessUtil.validatePrescriptionViewAccess(
                prescription.getDoctor().getUser().getEmail(),
                prescription.getPatient().getUser().getEmail()
        );

        return itemRepository.findByPrescriptionPrescriptionId(prescriptionId, pageable)
                .map(this::mapToResponse);
    }

    @Transactional
    public PrescriptionItemResponse updateItem(Integer itemId, PrescriptionItemRequest request) {
        PrescriptionItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Prescription item not found"));

        securityAccessUtil.validatePrescriptionOwnerAccess(
                item.getPrescription().getDoctor().getUser().getEmail()
        );

        item.setDosage(request.getDosage());
        item.setQuantity(request.getQuantity());
        item.setSpecialInstructions(request.getSpecialInstructions());

        return mapToResponse(itemRepository.save(item));
    }

    @Transactional
    public void deleteItem(Integer itemId) {
        PrescriptionItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with ID: " + itemId));

        // Only prescription owner doctor can delete item.
        // Admin cannot delete.
        securityAccessUtil.validatePrescriptionOwnerAccess(
                item.getPrescription().getDoctor().getUser().getEmail()
        );

        itemRepository.delete(item);
    }

    public PrescriptionItemResponse mapToResponse(PrescriptionItem item) {
        return PrescriptionItemResponse.builder()
                .prescriptionItemId(item.getPrescriptionItemId()) //
                .medicationId(item.getMedication().getMedicationId())
                .medicationName(item.getMedication().getName())
                .genericName(item.getMedication().getGenericName())
                .dosage(item.getDosage()) //
                .quantity(item.getQuantity()) //
                .specialInstructions(item.getSpecialInstructions()) //
                .createdAt(item.getCreatedAt()) //
                .updatedAt(item.getUpdatedAt()) //
                .build();
    }
}
