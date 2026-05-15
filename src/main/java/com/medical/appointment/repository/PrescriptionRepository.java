package com.medical.appointment.repository;

import com.medical.appointment.model.Prescription;
import com.medical.appointment.model.enums.PrescriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import com.medical.appointment.model.Prescription;
import com.medical.appointment.model.enums.PrescriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
    Optional<Prescription> findByAppointmentAppointmentId(Integer appointmentId);
    List<Prescription> findByPatientPatientId(Integer patientId);
    List<Prescription> findByDoctorDoctorId(Integer doctorId);
    List<Prescription> findByStatus(PrescriptionStatus status);
    boolean existsByAppointmentAppointmentId(Integer appointmentId);
}