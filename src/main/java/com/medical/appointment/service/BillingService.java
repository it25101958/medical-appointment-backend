package com.medical.appointment.service;

import com.medical.appointment.dto.billing.request.BillingRequest;
import com.medical.appointment.dto.billing.response.BillingResponse;
import com.medical.appointment.model.Appointment;
import com.medical.appointment.model.Billing;
import com.medical.appointment.model.Patient;
import com.medical.appointment.model.enums.BillingStatus;
import com.medical.appointment.repository.AppointmentRepository;
import com.medical.appointment.repository.BillingRepository;
import com.medical.appointment.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final BillingRepository billingRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;


    @Transactional
    public BillingResponse createBilling(BillingRequest request) {

        // Find appointment
        Appointment appointment = appointmentRepository
                .findById(request.getAppointmentId())
                .orElseThrow(() -> new RuntimeException(
                        "Appointment not found with id: " + request.getAppointmentId()));

        // Find patient
        Patient patient = patientRepository
                .findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException(
                        "Patient not found with id: " + request.getPatientId()));

        // Build billing from request
        Billing billing = new Billing();
        billing.setAppointment(appointment);
        billing.setPatient(patient);
        billing.setTotalAmount(request.getTotalAmount());
        billing.setDiscount(request.getDiscount());
        billing.setTax(request.getTax());
        billing.setBillingDate(request.getBillingDate());
        billing.setDueDate(request.getDueDate());
        billing.setFinalAmount(calculateFinalAmount(
                request.getTotalAmount(),
                request.getDiscount(),
                request.getTax()
        ));
        billing.setStatus(BillingStatus.PENDING);

        return convertToResponseDTO(billingRepository.save(billing));
    }


    @Transactional(readOnly = true)
    public BillingResponse getBillingById(Integer id) {
        return billingRepository.findById(id)
                .map(this::convertToResponseDTO)
                .orElseThrow(() -> new RuntimeException(
                        "Billing not found with id: " + id));
    }


    @Transactional(readOnly = true)
    public List<BillingResponse> getAllBillings() {
        return billingRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<BillingResponse> getBillingsByPatient(Integer patientId) {
        return billingRepository.findByPatient_PatientId(patientId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<BillingResponse> getBillingsByAppointment(Integer appointmentId) {
        return billingRepository.findByAppointment_AppointmentId(appointmentId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }


    @Transactional
    public BillingResponse updateBilling(Integer id, BillingRequest request) {
        return billingRepository.findById(id).map(existing -> {
            existing.setTotalAmount(request.getTotalAmount());
            existing.setDiscount(request.getDiscount());
            existing.setTax(request.getTax());
            existing.setFinalAmount(calculateFinalAmount(
                    request.getTotalAmount(),
                    request.getDiscount(),
                    request.getTax()
            ));
            existing.setDueDate(request.getDueDate());
            return convertToResponseDTO(billingRepository.save(existing));
        }).orElseThrow(() -> new RuntimeException(
                "Billing not found with id: " + id));
    }


    @Transactional
    public void deleteBilling(Integer id) {
        if (!billingRepository.existsById(id)) {
            throw new RuntimeException(
                    "Cannot delete: Billing not found with id: " + id);
        }
        billingRepository.deleteById(id);
    }


    private BigDecimal calculateFinalAmount(
            BigDecimal totalAmount,
            BigDecimal discount,
            BigDecimal tax) {
        BigDecimal safeDiscount = (discount != null) ? discount : BigDecimal.ZERO;
        BigDecimal safeTax = (tax != null) ? tax : BigDecimal.ZERO;
        return totalAmount.subtract(safeDiscount).add(safeTax);
    }


    private BillingResponse convertToResponseDTO(Billing billing) {
        BillingResponse response = new BillingResponse();
        response.setBillingId(billing.getBillingId());
        if (billing.getAppointment() != null) {
            response.setAppointmentId(billing.getAppointment().getAppointmentId());
        }
        response.setFinalAmount(billing.getFinalAmount());
        response.setBillingDate(billing.getBillingDate());
        response.setDueDate(billing.getDueDate());
        response.setStatus(billing.getStatus());
        return response;
    }
}