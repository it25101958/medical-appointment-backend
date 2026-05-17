package com.medical.appointment.repository;

import com.medical.appointment.model.RoomSchedule;
import com.medical.appointment.model.Doctor;
import com.medical.appointment.model.Room;
import com.medical.appointment.model.enums.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomScheduleRepository extends JpaRepository<RoomSchedule, Integer> {

    // 1. Automatic Shift Finder (Clean parameters, no JPQL math)
    @Query("SELECT rs FROM RoomSchedule rs WHERE rs.doctor = :doctor " +
            "AND rs.dayOfWeek = :dayOfWeek " +
            "AND rs.startTime <= :startTime " +
            "AND rs.endTime >= :endTime")
    Optional<RoomSchedule> findDoctorActiveShift(
            @Param("doctor") Doctor doctor,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    // 2. Clear out manual math symbols in validation queries to prevent Hibernate crash
    @Query("SELECT COUNT(rs) > 0 FROM RoomSchedule rs WHERE rs.room = :room " +
            "AND rs.dayOfWeek = :dayOfWeek " +
            "AND (:startTime < rs.endTime AND :endTime > rs.startTime)")
    boolean isRoomOccupied(
            @Param("room") Room room,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    List<RoomSchedule> findByDoctor_DoctorIdAndDayOfWeek(
            Integer doctorId,
            DayOfWeek dayOfWeek
    );

    @Query("SELECT COUNT(rs) > 0 FROM RoomSchedule rs WHERE rs.doctor = :doctor " +
            "AND rs.dayOfWeek = :dayOfWeek " +
            "AND (:startTime < rs.endTime AND :endTime > rs.startTime)")
    boolean isDoctorBusy(
            @Param("doctor") Doctor doctor,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    List<RoomSchedule> findByDoctorAndDayOfWeek(Doctor doctor, DayOfWeek dayOfWeek);

    @Query("SELECT rs FROM RoomSchedule rs, Appointment a WHERE a.room = rs.room " +
            "AND a.doctor = rs.doctor " +
            "AND a.appointmentId = :appointmentId")
    Optional<RoomSchedule> findByAppointmentAppointmentId(@Param("appointmentId") Integer appointmentId);

    List<RoomSchedule> findByDoctor(Doctor doctor);
}