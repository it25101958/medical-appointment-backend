package com.medical.appointment.dto.appoinment.response;

import com.medical.appointment.dto.feedback.response.FeedbackResponse;
import com.medical.appointment.dto.room.response.RoomResponse;
import com.medical.appointment.model.enums.AppointmentStatus;
import com.medical.appointment.model.enums.AppointmentType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class AppointmentResponse {

    private Integer appointmentId;

    private Integer patientId;
    private Integer doctorId;
    private Integer roomId;

    private AppointmentPatientSummaryResponse patient;
    private AppointmentDoctorSummaryResponse doctor;
    private RoomResponse room;

    private LocalDate appointmentDate;
    private LocalTime appointmentTime;

    private Integer appointmentNumber;
    private Integer durationMinutes;

    private AppointmentStatus status;
    private AppointmentType appointmentType;

    private String notes;

    private List<FeedbackResponse> feedbacks;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}