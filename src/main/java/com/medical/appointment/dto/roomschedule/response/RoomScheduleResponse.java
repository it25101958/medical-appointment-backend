package com.medical.appointment.dto.roomschedule.response;

import com.medical.appointment.model.enums.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomScheduleResponse {
    private Integer roomScheduleId;
    private String roomNumber;
    private String doctorName;
    private String appointmentNumber;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
