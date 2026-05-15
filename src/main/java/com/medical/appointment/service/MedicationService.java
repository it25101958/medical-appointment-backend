package com.medical.appointment.service;

import com.medical.appointment.dto.medication.request.MedicationRequest;
import com.medical.appointment.dto.medication.response.MedicationResponse;
import com.medical.appointment.model.Medication;
import com.medical.appointment.model.enums.MedicationStatus;
import com.medical.appointment.repository.MedicationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicationService {

    private final MedicationRepository medicationRepository;

    @Transactional
    public MedicationResponse createMedication(MedicationRequest request) {
        if (medicationRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Medication already exists with name: " + request.getName());
        }
        Medication medication = new Medication();
        mapRequestToEntity(request, medication);
        return mapToResponse(medicationRepository.save(medication));
    }

    @Transactional(readOnly = true)
    public MedicationResponse getMedicationById(int medicationId) {
        Medication medication = medicationRepository.findById(medicationId)
                .orElseThrow(() -> new EntityNotFoundException("Medication not found with id: " + medicationId));
        return mapToResponse(medication);
    }

    @Transactional(readOnly = true)
    public List<MedicationResponse> getAllMedications() {
        return medicationRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // --- Added Search & Filter Methods ---

    @Transactional(readOnly = true)
    public MedicationResponse getMedicationByName(String name) {
        Medication medication = medicationRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Medication not found with name: " + name));
        return mapToResponse(medication);
    }

    @Transactional(readOnly = true)
    public List<MedicationResponse> searchMedication(String name) {
        return medicationRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MedicationResponse> searchByGenericName(String genericName) {
        return medicationRepository.findByGenericNameContainingIgnoreCase(genericName).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MedicationResponse> getMedicationsByStatus(MedicationStatus status) {
        return medicationRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MedicationResponse> getMedicationsByDosageForm(String form) {
        return medicationRepository.findByDosageForm(form).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MedicationResponse> getMedicationsByManufacturer(String name) {
        return medicationRepository.findByManufacturerContainingIgnoreCase(name).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // --- Update & Status Methods ---

    @Transactional
    public MedicationResponse updateMedication(int medicationId, MedicationRequest request) {
        Medication existing = medicationRepository.findById(medicationId)
                .orElseThrow(() -> new EntityNotFoundException("Medication not found with id: " + medicationId));

        if (!existing.getName().equalsIgnoreCase(request.getName()) && medicationRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("New name already exists: " + request.getName());
        }

        mapRequestToEntity(request, existing);
        return mapToResponse(medicationRepository.save(existing));
    }

    @Transactional
    public MedicationResponse updateMedicationStatus(int medicationId, MedicationStatus status) {
        Medication medication = medicationRepository.findById(medicationId)
                .orElseThrow(() -> new EntityNotFoundException("Medication not found with id: " + medicationId));
        medication.setStatus(status);
        return mapToResponse(medicationRepository.save(medication));
    }

    @Transactional
    public void deleteMedication(int medicationId) {
        if (!medicationRepository.existsById(medicationId)) {
            throw new EntityNotFoundException("Medication not found with id: " + medicationId);
        }
        medicationRepository.deleteById(medicationId);
    }

    // --- Mapping Helpers ---

    private MedicationResponse mapToResponse(Medication medication) {
        return new MedicationResponse(
                medication.getMedicationId(),
                medication.getName(),
                medication.getGenericName(),
                medication.getManufacturer(),
                medication.getDosage(),
                medication.getDosageForm(),
                medication.getStatus(),
                medication.getCreatedAt(),
                medication.getUpdatedAt()
        );
    }

    private void mapRequestToEntity(MedicationRequest request, Medication medication) {
        medication.setName(request.getName());
        medication.setGenericName(request.getGenericName());
        medication.setManufacturer(request.getManufacturer());
        medication.setDosage(request.getDosage());
        medication.setDosageForm(request.getDosageForm());
        medication.setStatus(request.getStatus());
    }
}