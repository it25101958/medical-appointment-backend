package com.medical.appointment.dto.Appoinment.request;

import com.medical.appointment.model.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentStatusUpdateRequest {

    @NotNull(message = "Appointment status is required")
    private AppointmentStatus status;
}
