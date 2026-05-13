package com.medical.appointment.repository;

import com.medical.appointment.model.Appointment;
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

    @Query(value = """
        SELECT * FROM appointments a
        WHERE a.doctor_id = :doctorId
        AND a.appointment_date = :date
        AND a.appointment_time < :endTime
        AND ADDTIME(a.appointment_time, SEC_TO_TIME(a.duration_minutes * 60)) > :startTime
    """, nativeQuery = true)
    List<Appointment> findConflictingAppointments(
            @Param("doctorId") Integer doctorId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}