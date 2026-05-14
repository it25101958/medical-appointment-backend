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
    private final AppointmentRepository appointmentRepository;
    private final SecurityAccessUtil securityAccessUtil;

    private RoomScheduleResponse mapToResponse(RoomSchedule s) {
        return new RoomScheduleResponse(
                s.getRoomScheduleId(),
                s.getRoom().getRoomNumber(),
                "Dr. " + s.getDoctor().getUser().getLastName(),
                s.getAppointment().getAppointmentNumber(),
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
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        // Validations
        if (roomScheduleRepository.findByAppointmentAppointmentId(request.getAppointmentId()).isPresent()) {
            throw new IllegalStateException("Appointment already has an assigned room.");
        }
        if (roomScheduleRepository.isRoomOccupied(room, request.getDayOfWeek(), request.getStartTime(), request.getEndTime())) {
            throw new IllegalStateException("Room is already occupied at this time.");
        }
        if (roomScheduleRepository.isDoctorBusy(doctor, request.getDayOfWeek(), request.getStartTime(), request.getEndTime())) {
            throw new IllegalStateException("Doctor is already scheduled elsewhere at this time.");
        }

        RoomSchedule schedule = new RoomSchedule();
        schedule.setRoom(room);
        schedule.setDoctor(doctor);
        schedule.setAppointment(appointment);
        schedule.setDayOfWeek(request.getDayOfWeek());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());

        room.setStatus(RoomStatus.OCCUPIED);
        roomRepository.save(room);

        return mapToResponse(roomScheduleRepository.save(schedule));
    }



}
