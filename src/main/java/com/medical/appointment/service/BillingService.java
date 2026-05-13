package com.medical.appointment.service;

import com.medical.appointment.model.Billing;
import com.medical.appointment.model.enums.BillingStatus;
import com.medical.appointment.repository.BillingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BillingService {

    @Autowired
    private BillingRepository billingRepository;

    public Billing createBilling(Billing billing) {
        billing.setFinalAmount(calculateFinalAmount(
                billing.getTotalAmount(),
                billing.getDiscount(),
                billing.getTax()
        ));

        billing.setStatus(BillingStatus.PENDING);
        return billingRepository.save(billing);
    }

    public Optional<Billing> getBillingById(Integer id) {
        return billingRepository.findById(id);
    }

    public List<Billing> getAllBillings() {
        return billingRepository.findAll();
    }

    public List<Billing> getBillingsByPatient(Integer patientId) {
        return billingRepository.findByPatient_PatientId(patientId);
    }

    public List<Billing> getBillingsByAppointment(Integer appointmentId) {
        return billingRepository.findByAppointment_AppointmentId(appointmentId);
    }

    public Billing updateBilling(Integer id, Billing updatedBilling) {
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
            return billingRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Billing not found with id: " + id));
    }

    public void deleteBilling(Integer id) {
        billingRepository.deleteById(id);
    }

    private BigDecimal calculateFinalAmount(
            BigDecimal totalAmount,
            BigDecimal discount,
            BigDecimal tax) {
        BigDecimal safeDiscount = (discount != null) ? discount : BigDecimal.ZERO;
        BigDecimal safeTax = (tax != null) ? tax : BigDecimal.ZERO;

        return totalAmount
                .subtract(safeDiscount)
                .add(safeTax);
    }
}