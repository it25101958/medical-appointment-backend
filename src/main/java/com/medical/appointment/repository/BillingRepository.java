package com.medical.appointment.repository;

import com.medical.appointment.model.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Integer> {

    // Find all bills for a specific patient
    List<Billing> findByPatient_PatientId(Integer patientId);

    // Find all bills for a specific appointment
    List<Billing> findByAppointment_AppointmentId(Integer appointmentId);
}