package com.medical.appointment.controller;

import com.medical.appointment.dto.roomschedule.request.RoomScheduleRequest;
import com.medical.appointment.dto.roomschedule.response.RoomScheduleResponse;
import com.medical.appointment.service.RoomScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/room-schedule")
@RequiredArgsConstructor
public class RoomScheduleController {

    private final RoomScheduleService roomScheduleService;

    @PostMapping
    public ResponseEntity<RoomScheduleResponse> create(@Valid @RequestBody RoomScheduleRequest request) {
        return ResponseEntity.ok(roomScheduleService.createSchedule(request));
    }

    @GetMapping("/doctor/{doctorId}/today")
    public ResponseEntity<List<RoomScheduleResponse>> getDoctorToday(@PathVariable Integer doctorId) {
        return ResponseEntity.ok(roomScheduleService.getDoctorScheduleToday(doctorId));
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<RoomScheduleResponse> getByAppointment(@PathVariable Integer appointmentId) {
        return ResponseEntity.ok(roomScheduleService.getByAppointmentId(appointmentId));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<RoomScheduleResponse>> getAllDoctorSchedules(@PathVariable Integer doctorId) {
        List<RoomScheduleResponse> schedules = roomScheduleService.getAllDoctorSchedules(doctorId);
        return ResponseEntity.ok(schedules);
    }
}