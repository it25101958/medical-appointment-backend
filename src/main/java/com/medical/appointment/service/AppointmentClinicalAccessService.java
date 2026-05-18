package com.medical.appointment.service;

import com.medical.appointment.model.Appointment;
import com.medical.appointment.model.enums.AppointmentStatus;
import com.medical.appointment.security.SecurityAccessUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AppointmentClinicalAccessService {

    private final SecurityAccessUtil securityAccessUtil;

    public void validateDoctorCanModifyDuringAppointment(Appointment appointment) {

        securityAccessUtil.validateDoctorAccess();

        securityAccessUtil.validateStrictOwnership(
                appointment.getDoctor().getUser().getEmail()
        );

        validateAppointmentStatus(appointment);
        validateAppointmentTimeWindow(appointment);
    }

    private void validateAppointmentStatus(Appointment appointment) {
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("Cannot modify clinical records for a cancelled appointment.");
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot modify clinical records for a completed appointment.");
        }
    }

    private void validateAppointmentTimeWindow(Appointment appointment) {
        LocalDateTime startDateTime = LocalDateTime.of(
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime()
        );

        int durationMinutes = appointment.getDurationMinutes() != null
                ? appointment.getDurationMinutes()
                : 30;

        LocalDateTime endDateTime = startDateTime.plusMinutes(durationMinutes);
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(startDateTime)) {
            throw new IllegalStateException(
                    "You can add or modify prescriptions and lab orders only after the appointment starts."
            );
        }

        if (!now.isBefore(endDateTime)) {
            throw new IllegalStateException(            
                    "The appointment time has ended. Prescriptions and lab orders can no longer be changed."
            );
        }
    }
}
