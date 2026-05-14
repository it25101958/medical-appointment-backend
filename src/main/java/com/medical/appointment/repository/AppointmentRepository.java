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
    List<Appointment> findByPatient_Id(Integer patientId);

    // Find all appointments for a specific doctor
    List<Appointment> findByDoctor_Id(Integer doctorId);

    // Find appointments by appointment date
    List<Appointment> findByAppointmentDate(LocalDate appointmentDate);

    // Find appointments by appointment status
    List<Appointment> findByStatus(AppointmentStatus status);

    // Find appointments for a specific doctor on a specific date
    List<Appointment> findByDoctor_IdAndAppointmentDate(Integer doctorId, LocalDate appointmentDate);

    @Query("""
        SELECT a FROM Appointment a
        WHERE a.doctor.id = :doctorId
        AND a.appointmentDate = :date
        AND (
            a.appointmentTime < :endTime
            AND (a.appointmentTime + a.durationMinutes * 1/1440.0) > :startTime
        )
    """)
    List<Appointment> findConflictingAppointments(
            @Param("doctorId") Integer doctorId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}