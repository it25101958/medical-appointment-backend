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
    @Query("SELECT a FROM Appointment a WHERE a.doctor.doctorId = :doctorId " +
            "AND a.appointmentDate = :date " +
            "AND a.status <> com.medical.appointment.model.enums.AppointmentStatus.CANCELLED " +
            "AND a.appointmentTime < :endTime " +
            "AND :startTime <= a.appointmentTime")
    List<Appointment> findConflictingAppointments(
            @Param("doctorId") Integer doctorId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    @Query("""
    SELECT a FROM Appointment a
    WHERE a.doctor.doctorId = :doctorId
    AND a.appointmentDate = :date
    AND a.status <> 'CANCELLED'
""")
    List<Appointment> findBookedAppointmentsByDoctorAndDate(
            @Param("doctorId") Integer doctorId,
            @Param("date") LocalDate date
    );

    @Query("SELECT MAX(a.appointmentNumber) FROM Appointment a WHERE a.doctor.doctorId = :doctorId AND a.appointmentDate = :date")
    Integer findMaxAppointmentNumberByDoctorAndDate(@Param("doctorId") Integer doctorId, @Param("date") LocalDate date);

    List<Appointment> findAllByDoctorUserEmail(String email);

    List<Appointment> findByDoctorDoctorIdAndAppointmentDateAndStatusNotOrderByAppointmentTimeAsc(
            Integer doctorId,
            LocalDate appointmentDate,
            AppointmentStatus status
    );

    List<Appointment> findByDoctorUserEmailAndAppointmentDateAndStatusNotOrderByAppointmentTimeAsc(
            String email,
            LocalDate appointmentDate,
            AppointmentStatus status
    );

    List<Appointment> findAllByPatientUserEmailOrderByAppointmentDateDescAppointmentTimeDesc(String email);

    List<Appointment> findAllByDoctorUserEmailOrderByAppointmentDateDescAppointmentTimeDesc(String email);
}