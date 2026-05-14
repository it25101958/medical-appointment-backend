package com.medical.appointment.service;

import com.medical.appointment.dto.appoinment.request.appointmentCreateRequest;
import com.medical.appointment.dto.appoinment.request.appointmentUpdateRequest;
import com.medical.appointment.dto.appoinment.request.appointmentStatusUpdateRequest;
import com.medical.appointment.model.Appointment;
import com.medical.appointment.model.enums.AppointmentStatus;
import com.medical.appointment.repository.AppointmentRepository;

import com.medical.appointment.repository.PatientRepository;
import com.medical.appointment.repository.DoctorRepository;
import com.medical.appointment.repository.RoomRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class AppoinmentService {

    private final AppointmentRepository appointmentRepository;

    public Appointment createAppointment(appointmentCreateRequest request) {
        Appointment appointment = new Appointment();

        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setDurationMinutes(request.getDurationMinutes());
        appointment.setAppointmentType(request.getAppointmentType());
        appointment.setNotes(request.getNotes());
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment getAppointmentById(Integer appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    public Appointment updateAppointment(Integer appointmentId, appointmentUpdateRequest request) {
        Appointment appointment = getAppointmentById(appointmentId);

        if (request.getAppointmentDate() != null) {
            appointment.setAppointmentDate(request.getAppointmentDate());
        }

        if (request.getAppointmentTime() != null) {
            appointment.setAppointmentTime(request.getAppointmentTime());
        }

        if (request.getDurationMinutes() != null) {
            appointment.setDurationMinutes(request.getDurationMinutes());
        }

        if (request.getAppointmentType() != null) {
            appointment.setAppointmentType(request.getAppointmentType());
        }

        if (request.getNotes() != null) {
            appointment.setNotes(request.getNotes());
        }

        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointmentStatus(Integer appointmentId, appointmentStatusUpdateRequest request) {
        Appointment appointment = getAppointmentById(appointmentId);
        appointment.setStatus(request.getStatus());

        return appointmentRepository.save(appointment);
    }

    public void cancelAppointment(Integer appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);
        appointment.setStatus(AppointmentStatus.CANCELLED);

        appointmentRepository.save(appointment);
    }
}