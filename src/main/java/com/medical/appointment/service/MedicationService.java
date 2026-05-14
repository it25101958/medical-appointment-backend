package com.medical.appointment.service;

import com.medical.appointment.model.Medication;
import com.medical.appointment.model.MedicationStatus;
import com.medical.appointment.repository.MedicationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicationService {

    private final MedicationRepository medicationRepository;

    //add new medication | create
    @Transactional
    public Medication createMedication(Medication medication) {
        if (medicationRepository.existsByName(medication.getName())) {
            throw new IllegalArgumentException(
                    "Medication already exists with name: " + medication.getName());
        }
        medication.setStatus(String.valueOf(MedicationStatus.ACTIVE));
        return medicationRepository.save(medication);
    }

    // Search medicine | read
    public Medication getMedicationById(int medicationId) {
        return medicationRepository.findById(medicationId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Medication not found with id: " + medicationId));
    }

    public List<Medication> getAllMedications() {
        return medicationRepository.findAll();
    }

    public Medication getMedicationByName(String name) {
        return medicationRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Medication not found with name: " + name));
    }

    public List<Medication> searchMedication(String name) {
        return medicationRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Medication> searchByGenericName(String genericName) {
        return medicationRepository.findByGenericNameContainingIgnoreCase(genericName);
    }

    public List<Medication> getMedicationsByStatus(MedicationStatus status) {
        return medicationRepository.findByStatus(status);
    }

    public List<Medication> getMedicationsByDosageForm(String dosageForm) {
        return medicationRepository.findByDosageForm(dosageForm);
    }

    public List<Medication> getMedicationsByManufacturer(String manufacturer) {
        return medicationRepository.findByManufacturerContainingIgnoreCase(manufacturer);
    }

    // update the current medicine | update
    @Transactional
    public Medication updateMedication(int medicationId, Medication updated) {
        Medication existing = getMedicationById(medicationId);
        existing.setName(updated.getName());
        existing.setGenericName(updated.getGenericName());
        existing.setManufacturer(updated.getManufacturer());
        existing.setDosage(updated.getDosage());
        existing.setDosageForm(updated.getDosageForm());
        return medicationRepository.save(existing);
    }

    @Transactional
    public Medication updateMedicationStatus(int medicationId, MedicationStatus status) {
        Medication existing = getMedicationById(medicationId);
        existing.setStatus(String.valueOf(status));
        return medicationRepository.save(existing);
    }

    // remove current medication from exiting list | delete
    @Transactional
    public void deleteMedication(int medicationId) {
        if (!medicationRepository.existsById(medicationId)) {
            throw new EntityNotFoundException(
                    "Medication not found with id: " + medicationId);
        }
        medicationRepository.deleteById(medicationId);
    }
}