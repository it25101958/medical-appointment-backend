package com.medical.appointment.controller;

import com.medical.appointment.dto.appoinment.request.appointmentCreateRequest;
import com.medical.appointment.dto.appoinment.request.appointmentStatusUpdateRequest;
import com.medical.appointment.dto.appoinment.request.appointmentUpdateRequest;
import com.medical.appointment.dto.appoinment.response.appointmentResponse;
import com.medical.appointment.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    // Create a new appointment (Returns 201 Created status code)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public appointmentResponse createAppointment(
            @Valid @RequestBody appointmentCreateRequest request
    ) {
        return appointmentService.createAppointment(request);
    }

    // Get all appointments (Admins/Staff see all, Doctors see only their own)
    @GetMapping
    public List<appointmentResponse> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    // Get appointment details by ID
    @GetMapping("/{id}")
    public appointmentResponse getAppointmentById(@PathVariable Integer id) {
        return appointmentService.getAppointmentById(id);
    }

    @PutMapping("/{id}")
    public appointmentResponse updateAppointment(
            @PathVariable Integer id,
            @Valid @RequestBody appointmentUpdateRequest request
    ) {
        return appointmentService.updateAppointment(id, request);
    }

    // Update appointment status (Restricted to Admin and Staff)
    @PatchMapping("/{id}/status")
    public appointmentResponse updateAppointmentStatus(
            @PathVariable Integer id,
            @Valid @RequestBody appointmentStatusUpdateRequest request
    ) {
        return appointmentService.updateAppointmentStatus(id, request);
    }

    // Cancel appointment (Restricted to Admin and Staff, returns 240 No Content status)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelAppointment(@PathVariable Integer id) {
        appointmentService.cancelAppointment(id);
    }
}