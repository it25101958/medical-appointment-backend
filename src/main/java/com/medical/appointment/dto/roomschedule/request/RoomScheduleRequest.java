package com.medical.appointment.dto.roomschedule.request;

import com.medical.appointment.model.enums.DayOfWeek;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@Data
public class RoomScheduleRequest {

    @NotNull(message = "Doctor ID is required")
    private Integer doctorId;

    @NotNull(message = "Room ID is required")
    private Integer roomId;

    @NotNull(message = "Day of week is required")
    private DayOfWeek dayOfWeek;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;
}
