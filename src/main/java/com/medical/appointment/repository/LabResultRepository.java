package com.medical.appointment.repository;

import com.medical.appointment.model.LabResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabResultRepository extends JpaRepository<LabResult, Long> {
    List<LabResult> findByPatientId(Long patientId);

    List<LabResult> findByAppointmentId(Long appointmentId);
}
