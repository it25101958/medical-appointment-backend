package com.medical.appointment.dto.appoinment.request;

import com.medical.appointment.model.enums.AppointmentType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class appointmentUpdateRequest {

    private Integer doctorId;

    private Integer roomId;

    @FutureOrPresent(message = "Appointment date cannot be in the past")
    private LocalDate appointmentDate;

    private LocalTime appointmentTime;

    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer durationMinutes;

    private AppointmentType appointmentType;

    @Size(max = 255, message = "Notes cannot exceed 255 characters")
    private String notes;
}
