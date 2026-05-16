package com.medical.appointment.repository;

import com.medical.appointment.model.Appointment;
import com.medical.appointment.model.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByPatient_PatientId(Integer patientId);
    List<Appointment> findByDoctor_DoctorId(Integer doctorId);

    // Find all appointments for a specific patient
    List<Appointment> findByPatient_patientId(Integer patientId);

    // Find all appointments for a specific doctor
    List<Appointment> findByDoctor_doctorId(Integer doctorId);

    // Find appointments by appointment date
    List<Appointment> findByAppointmentDate(LocalDate appointmentDate);

    // Find appointments by appointment status
    List<Appointment> findByStatus(AppointmentStatus status);

    // Find appointments for a specific doctor on a specific date
    List<Appointment> findByDoctor_doctorIdAndAppointmentDate(Integer doctorId, LocalDate appointmentDate);

    @Query("SELECT lo FROM LabOrder lo JOIN lo.appointment a JOIN lo.items i " +
            "WHERE (:patientId IS NULL OR a.patient.patientId = :patientId) " +
            "AND (:status IS NULL OR i.status = :status) " +
            "AND (:startDate IS NULL OR lo.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR lo.createdAt <= :endDate)")
    List<Appointment> findConflictingAppointments(
            @Param("doctorId") Integer doctorId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}