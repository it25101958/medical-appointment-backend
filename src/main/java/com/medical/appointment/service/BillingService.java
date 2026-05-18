package com.medical.appointment.service;

import com.medical.appointment.dto.billing.response.BillingResponse;
import com.medical.appointment.model.Billing;
import com.medical.appointment.model.enums.BillingStatus;
import com.medical.appointment.repository.BillingRepository;
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

    @Transactional
    public BillingResponse createBilling(Billing billing) {
        billing.setFinalAmount(calculateFinalAmount(
                billing.getTotalAmount(),
                billing.getDiscount(),
                billing.getTax()
        ));

        billing.setStatus(BillingStatus.PENDING);
        Billing savedBilling = billingRepository.save(billing);
        return convertToResponseDTO(savedBilling);
    }

    @Transactional(readOnly = true)
    public BillingResponse getBillingById(Integer id) {
        return billingRepository.findById(id)
                .map(this::convertToResponseDTO)
                .orElseThrow(() -> new RuntimeException("Billing not found with id: " + id));
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
    public BillingResponse updateBilling(Integer id, Billing updatedBilling) {
        return billingRepository.findById(id).map(existing -> {
            existing.setTotalAmount(updatedBilling.getTotalAmount());
            existing.setDiscount(updatedBilling.getDiscount());
            existing.setTax(updatedBilling.getTax());
            existing.setFinalAmount(calculateFinalAmount(
                    updatedBilling.getTotalAmount(),
                    updatedBilling.getDiscount(),
                    updatedBilling.getTax()
            ));
            existing.setDueDate(updatedBilling.getDueDate());
            existing.setStatus(updatedBilling.getStatus());
            return convertToResponseDTO(billingRepository.save(existing));
        }).orElseThrow(() -> new RuntimeException("Billing not found with id: " + id));
    }

    @Transactional
    public void deleteBilling(Integer id) {
        if (!billingRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete: Billing not found with id: " + id);
        }
        billingRepository.deleteById(id);
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

    private BigDecimal calculateFinalAmount(BigDecimal totalAmount, BigDecimal discount, BigDecimal tax) {
        BigDecimal safeDiscount = (discount != null) ? discount : BigDecimal.ZERO;
        BigDecimal safeTax = (tax != null) ? tax : BigDecimal.ZERO;
        return totalAmount.subtract(safeDiscount).add(safeTax);
    }
}