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

    List<Appointment> findByPatient_Id(Integer patientId);
    List<Appointment> findByDoctor_Id(Integer doctorId);


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

