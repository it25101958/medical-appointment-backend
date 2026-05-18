package com.medical.appointment.service;

import com.medical.appointment.dto.roomschedule.request.RoomScheduleRequest;
import com.medical.appointment.dto.roomschedule.response.RoomScheduleResponse;
import com.medical.appointment.model.*;
import com.medical.appointment.model.enums.AccessLevel;
import com.medical.appointment.model.enums.DayOfWeek;
import com.medical.appointment.model.enums.RoomStatus;
import com.medical.appointment.repository.*;
import com.medical.appointment.security.SecurityAccessUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomScheduleService {

    private final RoomScheduleRepository roomScheduleRepository;
    private final RoomRepository roomRepository;
    private final DoctorRepository doctorRepository;
    private final SecurityAccessUtil securityAccessUtil;

    private RoomScheduleResponse mapToResponse(RoomSchedule s) {
        return new RoomScheduleResponse(
                s.getRoomScheduleId(),
                s.getRoom().getRoomNumber(),
                "Dr. " + s.getDoctor().getUser().getLastName(),
                1,
                s.getDayOfWeek(),
                s.getStartTime(),
                s.getEndTime(),
                s.getCreatedAt(),
                s.getUpdatedAt()
        );
    }

    @Transactional
    public RoomScheduleResponse createSchedule(RoomScheduleRequest request) {
        securityAccessUtil.validateAdminLevel(AccessLevel.FULL, AccessLevel.SUPER_ADMIN);

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

        // FIXED VALIDATIONS: Ensure no other doctor has claimed this block, and this doctor isn't split between rooms
        if (roomScheduleRepository.isRoomOccupied(room, request.getDayOfWeek(), request.getStartTime(), request.getEndTime())) {
            throw new IllegalStateException("This room is already allocated to another doctor's shift during this time block.");
        }
        if (roomScheduleRepository.isDoctorBusy(doctor, request.getDayOfWeek(), request.getStartTime(), request.getEndTime())) {
            throw new IllegalStateException("This doctor is already scheduled to work in a different room during this time slot.");
        }

        RoomSchedule schedule = new RoomSchedule();
        schedule.setRoom(room);
        schedule.setDoctor(doctor);
        schedule.setDayOfWeek(request.getDayOfWeek());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        room.setStatus(RoomStatus.OCCUPIED);
        roomRepository.save(room);

        return mapToResponse(roomScheduleRepository.save(schedule));
    }

    @Transactional(readOnly = true)
    public List<RoomScheduleResponse> getDoctorScheduleToday(Integer doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

        DayOfWeek today = DayOfWeek.valueOf(LocalDate.now().getDayOfWeek().name());
        return roomScheduleRepository.findByDoctorAndDayOfWeek(doctor, today)
                .stream().map(this::mapToResponse).toList();
    }

    @Transactional(readOnly = true)
    public RoomScheduleResponse getByAppointmentId(Integer appointmentId) {
        return roomScheduleRepository.findByAppointmentAppointmentId(appointmentId)
                .map(this::mapToResponse)
                .orElseThrow(() -> new EntityNotFoundException("No shift schedule matches this target appointment ID context"));
    }

    @Transactional(readOnly = true)
    public List<RoomScheduleResponse> getAllDoctorSchedules(Integer doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + doctorId));

        return roomScheduleRepository.findByDoctor(doctor)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public RoomScheduleResponse updateSchedule(Integer scheduleId, RoomScheduleRequest request) {
        securityAccessUtil.validateAdminLevel(AccessLevel.FULL, AccessLevel.SUPER_ADMIN);

        RoomSchedule schedule = roomScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

        // Check conflicts
        if (roomScheduleRepository.isRoomOccupied(room, request.getDayOfWeek(), request.getStartTime(), request.getEndTime())) {
            throw new IllegalStateException("This room is already allocated for this time block.");
        }
        if (roomScheduleRepository.isDoctorBusy(doctor, request.getDayOfWeek(), request.getStartTime(), request.getEndTime())) {
            throw new IllegalStateException("Doctor is busy in another room at this time.");
        }

        schedule.setRoom(room);
        schedule.setDoctor(doctor);
        schedule.setDayOfWeek(request.getDayOfWeek());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());

        return mapToResponse(roomScheduleRepository.save(schedule));
    }

    /** DELETE */
    @Transactional
    public void deleteSchedule(Integer scheduleId) {
        securityAccessUtil.validateAdminLevel(AccessLevel.FULL, AccessLevel.SUPER_ADMIN);

        RoomSchedule schedule = roomScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));

        // Optional: free up room
        Room room = schedule.getRoom();
        room.setStatus(RoomStatus.AVAILABLE);
        roomRepository.save(room);

        roomScheduleRepository.delete(schedule);
    }

}