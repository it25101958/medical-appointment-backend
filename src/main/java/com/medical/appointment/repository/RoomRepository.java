package com.medical.appointment.repository;


import com.medical.appointment.model.Doctor;
import com.medical.appointment.model.Room;
import com.medical.appointment.model.RoomSchedule;
import com.medical.appointment.model.enums.DayOfWeek;
import com.medical.appointment.model.enums.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;



@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    List<Room> findByStatus(RoomStatus status);
    List<Room> findByStatus(String status);
    List<Room> findByRoomType(String roomType);
    Optional<Room> findByRoomNumber(String roomNumber);
    boolean existsByRoomNumber(String roomNumber);

    @Query("""
    SELECT rs FROM RoomSchedule rs
    WHERE rs.doctor = :doctor
      AND rs.dayOfWeek = :dayOfWeek
      AND rs.startTime <= :startTime
      AND rs.endTime >= :endTime
""")
    Optional<RoomSchedule> findDoctorActiveShift(
            Doctor doctor,
            DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endTime
    );


}