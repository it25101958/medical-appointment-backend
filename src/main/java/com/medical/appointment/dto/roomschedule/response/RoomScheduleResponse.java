package com.medical.appointment.dto.roomschedule.response;

import com.medical.appointment.model.enums.DayOfWeek;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    public RoomScheduleResponse(Integer roomScheduleId, @NotBlank @Size(max = 10) String roomNumber, @NotNull(message = "Last name cannot be null") String doctorName, @Min(value = 1, message = "Appointment number must be at least 1") Integer appointmentNumber, @NotNull DayOfWeek dayOfWeek, @NotNull LocalTime startTime, @NotNull LocalTime endTime, @NotNull LocalDateTime createdAt, @NotNull LocalDateTime updatedAt) {
    }
}
