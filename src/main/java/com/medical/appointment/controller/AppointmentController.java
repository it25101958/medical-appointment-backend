package com.medical.appointment.controller;

import com.medical.appointment.dto.appoinment.request.appointmentCreateRequest;
import com.medical.appointment.dto.appoinment.request.appointmentStatusUpdateRequest;
import com.medical.appointment.dto.appoinment.request.appointmentUpdateRequest;
import com.medical.appointment.model.Appointment;
import com.medical.appointment.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    // Create new appointment
    @PostMapping
    public Appointment createAppointment(
            @Valid @RequestBody appointmentCreateRequest request
    ) {
        return appointmentService.createAppointment(request);
    }

    // Get all appointments
    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    // Get appointment by ID
    @GetMapping("/{id}")
    public Appointment getAppointmentById(@PathVariable Integer id) {
        return appointmentService.getAppointmentById(id);
    }

    // Update appointment
    @PutMapping("/{id}")
    public Appointment updateAppointment(
            @PathVariable Integer id,
            @Valid @RequestBody appointmentUpdateRequest request
    ) {
        return appointmentService.updateAppointment(id, request);
    }

    // Update appointment status
    @PatchMapping("/{id}/status")
    public Appointment updateAppointmentStatus(
            @PathVariable Integer id,
            @Valid @RequestBody appointmentStatusUpdateRequest request
    ) {
        return appointmentService.updateAppointmentStatus(id, request);
    }

    // Cancel appointment
    @DeleteMapping("/{id}")
    public String cancelAppointment(@PathVariable Integer id) {

        appointmentService.cancelAppointment(id);

        return "Appointment cancelled successfully";
    }
}