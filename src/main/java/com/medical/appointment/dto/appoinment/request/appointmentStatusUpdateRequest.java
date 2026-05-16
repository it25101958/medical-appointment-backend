package com.medical.appointment.dto.appoinment.request;

import com.medical.appointment.model.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class appointmentStatusUpdateRequest {

    @NotNull(message = "Appointment status is required")
    private AppointmentStatus status;
}
