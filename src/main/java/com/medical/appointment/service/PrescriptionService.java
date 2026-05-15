package com.medical.appointment.service;

import com.medical.appointment.dto.prescription.request.PrescriptionRequest;
import com.medical.appointment.dto.prescription.response.PrescriptionResponse;
import com.medical.appointment.dto.prescriptionItem.response.PrescriptionItemResponse;
import com.medical.appointment.model.*;
import com.medical.appointment.model.enums.AccessLevel;
import com.medical.appointment.repository.*;
import com.medical.appointment.security.SecurityAccessUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicationRepository medicationRepository;
    private final SecurityAccessUtil securityAccessUtil;

    @Transactional
    public PrescriptionResponse createPrescription(PrescriptionRequest request) {
        securityAccessUtil.validateDoctorAccess();
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found with ID: " + request.getAppointmentId()));

        if (prescriptionRepository.existsByAppointmentAppointmentId(request.getAppointmentId())) {
            throw new IllegalStateException("A prescription already exists for this appointment.");
        }

        Prescription prescription = new Prescription();
        prescription.setAppointment(appointment);
        prescription.setDoctor(appointment.getDoctor());
        prescription.setPatient(appointment.getPatient());
        prescription.setPrescriptionDate(LocalDate.now());
        prescription.setStatus(request.getStatus());
        prescription.setNotes(request.getNotes());

        List<PrescriptionItem> items = request.getItems().stream().map(itemDto -> {
            Medication medication = medicationRepository.findById(itemDto.getMedicationId())
                    .orElseThrow(() -> new EntityNotFoundException("Medication not found ID: " + itemDto.getMedicationId()));

            PrescriptionItem item = new PrescriptionItem();
            item.setPrescription(prescription);
            item.setMedication(medication);
            item.setDosage(itemDto.getDosage());
            item.setQuantity(itemDto.getQuantity());
            item.setSpecialInstructions(itemDto.getSpecialInstructions());
            return item;
        }).toList();

        prescription.setItems(items);
        Prescription savedPrescription = prescriptionRepository.save(prescription);

        return mapToResponse(savedPrescription);
    }

    @Transactional(readOnly = true)
    public List<PrescriptionResponse> getAllPrescriptions() {
        securityAccessUtil.validateAdminLevel(AccessLevel.SUPER_ADMIN, AccessLevel.FULL);
        return prescriptionRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PrescriptionResponse getPrescriptionById(Integer id) {
        Prescription p = prescriptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prescription not found with ID: " + id));
        String ownerEmail = p.getDoctor().getUser().getEmail();
        securityAccessUtil.validateDoctorAccess();
        securityAccessUtil.validateOwnership(ownerEmail);
        return mapToResponse(p);
    }

    @Transactional(readOnly = true)
    public PrescriptionResponse getByAppointmentId(Integer appointmentId) {
        Prescription p = prescriptionRepository.findByAppointmentAppointmentId(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("No prescription for appointment: " + appointmentId));

        securityAccessUtil.validateDoctorAccess();
        String ownerEmail = p.getDoctor().getUser().getEmail();
        securityAccessUtil.validateOwnership(ownerEmail);
        return mapToResponse(p);
    }

    @Transactional
    public PrescriptionResponse updatePrescription(Integer id, PrescriptionRequest request) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prescription not found with ID: " + id));

        securityAccessUtil.validateDoctorAccess();
        securityAccessUtil.validateOwnership(prescription.getDoctor().getUser().getEmail());

        prescription.setStatus(request.getStatus());
        prescription.setNotes(request.getNotes());
        prescription.getItems().clear();

        List<PrescriptionItem> newItems = request.getItems().stream().map(itemDto -> {
            Medication medication = medicationRepository.findById(itemDto.getMedicationId())
                    .orElseThrow(() -> new EntityNotFoundException("Medication not found ID: " + itemDto.getMedicationId()));

            PrescriptionItem item = new PrescriptionItem();
            item.setPrescription(prescription);
            item.setMedication(medication);
            item.setDosage(itemDto.getDosage());
            item.setQuantity(itemDto.getQuantity());
            item.setSpecialInstructions(itemDto.getSpecialInstructions());
            return item;
        }).toList();

        prescription.getItems().addAll(newItems);

        return mapToResponse(prescriptionRepository.save(prescription));
    }

    @Transactional
    public void deletePrescription(Integer id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prescription not found with ID: " + id));

        securityAccessUtil.validateDoctorAccess();
        securityAccessUtil.validateOwnership(prescription.getDoctor().getUser().getEmail());
        prescriptionRepository.delete(prescription);
    }


    private PrescriptionResponse mapToResponse(Prescription p) {
        List<PrescriptionItemResponse> itemDtos = p.getItems().stream()
                .map(item -> PrescriptionItemResponse.builder()
                        .prescriptionItemId(item.getPrescriptionItemId())
                        .medicationId(item.getMedication().getMedicationId())
                        .medicationName(item.getMedication().getName())
                        .genericName(item.getMedication().getGenericName())
                        .dosage(item.getDosage())
                        .quantity(item.getQuantity())
                        .specialInstructions(item.getSpecialInstructions())
                        .createdAt(item.getCreatedAt())
                        .updatedAt(item.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());

        return new PrescriptionResponse(
                p.getPrescriptionId(),
                p.getAppointment().getAppointmentId(),
                p.getDoctor().getUser().getFirstName() + " " + p.getDoctor().getUser().getLastName(),
                p.getPatient().getUser().getFirstName() + " " + p.getPatient().getUser().getLastName(),
                p.getPrescriptionDate(),
                p.getStatus(),
                p.getNotes(),
                itemDtos,
                p.getCreatedAt()
        );
    }
}