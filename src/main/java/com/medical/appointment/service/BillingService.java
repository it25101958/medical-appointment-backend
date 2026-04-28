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

    //  CREATE - Save a new bill
    public Billing createBilling(Billing billing) {
        // Calculate finalAmount before saving
        billing.setFinalAmount(calculateFinalAmount(
                billing.getTotalAmount(),
                billing.getDiscount(),
                billing.getTax()
        ));
        // Set default status to PENDING
        billing.setStatus(BillingStatus.PENDING);
        return billingRepository.save(billing);
    }

    //  READ - Get one bill by ID
    public Optional<Billing> getBillingById(Integer id) {
        return billingRepository.findById(id);
    }

    //  READ - Get all bills
    public List<Billing> getAllBillings() {
        return billingRepository.findAll();
    }

    //  READ - Get all bills for a specific patient
    public List<Billing> getBillingsByPatient(Integer patientId) {
        return billingRepository.findByPatient_PatientId(patientId);
    }

    //  READ - Get all bills for a specific appointment
    public List<Billing> getBillingsByAppointment(Integer appointmentId) {
        return billingRepository.findByAppointment_AppointmentId(appointmentId);
    }

    //  UPDATE - Update an existing bill
    public Billing updateBilling(Integer id, Billing updatedBilling) {
        return billingRepository.findById(id).map(existing -> {
            existing.setTotalAmount(updatedBilling.getTotalAmount());
            existing.setDiscount(updatedBilling.getDiscount());
            existing.setTax(updatedBilling.getTax());
            // Recalculate finalAmount after update
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

    //  DELETE - Delete a bill by ID
    public void deleteBilling(Integer id) {
        billingRepository.deleteById(id);
    }

    //  CORE LOGIC: finalAmount = totalAmount - discount + tax
    private BigDecimal calculateFinalAmount(
            BigDecimal totalAmount,
            BigDecimal discount,
            BigDecimal tax) {

        // If discount or tax is null, treat as 0
        BigDecimal safeDiscount = (discount != null) ? discount : BigDecimal.ZERO;
        BigDecimal safeTax = (tax != null) ? tax : BigDecimal.ZERO;

        return totalAmount
                .subtract(safeDiscount)
                .add(safeTax);
    }
}