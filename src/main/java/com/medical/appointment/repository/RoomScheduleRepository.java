package com.medical.appointment.repository;

import com.medical.appointment.model.Doctor;
import com.medical.appointment.model.Room;
import com.medical.appointment.model.RoomSchedule;
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

    Optional<RoomSchedule> findByAppointmentAppointmentId(Integer appointmentId);

    // Overlap Logic: (NewStart < ExistingEnd) AND (NewEnd > ExistingStart)
    @Query("SELECT COUNT(rs) > 0 FROM RoomSchedule rs WHERE rs.room = :room " +
            "AND rs.dayOfWeek = :day AND (:start < rs.endTime AND :end > rs.startTime)")
    boolean isRoomOccupied(@Param("room") Room room, @Param("day") DayOfWeek day,
                           @Param("start") LocalTime start, @Param("end") LocalTime end);

    @Query("SELECT COUNT(rs) > 0 FROM RoomSchedule rs WHERE rs.doctor = :doctor " +
            "AND rs.dayOfWeek = :day AND (:start < rs.endTime AND :end > rs.startTime)")
    boolean isDoctorBusy(@Param("doctor") Doctor doctor, @Param("day") DayOfWeek day,
                         @Param("start") LocalTime start, @Param("end") LocalTime end);

    List<RoomSchedule> findByDoctorAndDayOfWeek(Doctor doctor, DayOfWeek dayOfWeek);
}
