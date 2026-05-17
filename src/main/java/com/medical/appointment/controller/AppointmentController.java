package com.medical.appointment.controller;

import com.medical.appointment.dto.appoinment.request.AppointmentCreateRequest;
import com.medical.appointment.dto.appoinment.request.AppointmentStatusUpdateRequest;
import com.medical.appointment.dto.appoinment.request.AppointmentUpdateRequest;
import com.medical.appointment.dto.appoinment.response.AppointmentResponse;
import com.medical.appointment.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    // Create a new appointment (Returns 201 Created status code)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponse createAppointment(
            @Valid @RequestBody AppointmentCreateRequest request
    ) {
        return appointmentService.createAppointment(request);
    }

    // Get all appointments (Admins/Staff see all, Doctors see only their own)
    @GetMapping
    public List<AppointmentResponse> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    // Get appointment details by ID
    @GetMapping("/{id}")
    public AppointmentResponse getAppointmentById(@PathVariable Integer id) {
        return appointmentService.getAppointmentById(id);
    }

    @GetMapping("/my")
    public ResponseEntity<List<AppointmentResponse>> getMyAppointments() {
        return ResponseEntity.ok(appointmentService.getMyAppointments());
    }

    @GetMapping("/my/today")
    public List<AppointmentResponse> getMyTodayAppointments() {
        return appointmentService.getMyTodayAppointments();
    }

    @PutMapping("/{id}")
    public AppointmentResponse updateAppointment(
            @PathVariable Integer id,
            @Valid @RequestBody AppointmentUpdateRequest request
    ) {
        return appointmentService.updateAppointment(id, request);
    }

    @GetMapping("/available-slots")
    public ResponseEntity<List<LocalTime>> getAvailableSlots(
            @RequestParam Integer doctorId,
            @RequestParam LocalDate date
    ) {
        return ResponseEntity.ok(
                appointmentService.getAvailableSlots(doctorId, date)
        );
    }

    // Update appointment status (Restricted to Admin and Staff)
    @PatchMapping("/{id}/status")
    public AppointmentResponse updateAppointmentStatus(
            @PathVariable Integer id,
            @Valid @RequestBody AppointmentStatusUpdateRequest request
    ) {
        return appointmentService.updateAppointmentStatus(id, request);
    }

    @GetMapping("/doctor/{doctorId}/today")
    public List<AppointmentResponse> getTodayAppointmentsByDoctor(
            @PathVariable Integer doctorId
    ) {
        return appointmentService.getTodayAppointmentsByDoctor(doctorId);
    }

    // Cancel appointment (Restricted to Admin and Staff, returns 240 No Content status)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelAppointment(@PathVariable Integer id) {
        appointmentService.cancelAppointment(id);
    }
}