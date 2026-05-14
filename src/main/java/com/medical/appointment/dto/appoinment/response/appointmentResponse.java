package com.medical.appointment.dto.appoinment.response;

import com.medical.appointment.model.enums.AppointmentStatus;
import com.medical.appointment.model.enums.AppointmentType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class appointmentResponse {

    private Integer appointmentId;

    private Integer patientId;

    private Integer doctorId;

    private Integer roomId;

    private LocalDate appointmentDate;

    private LocalTime appointmentTime;

    private Integer durationMinutes;

    private AppointmentStatus status;

    private AppointmentType appointmentType;

    private String notes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
