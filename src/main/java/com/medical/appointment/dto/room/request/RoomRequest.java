package com.medical.appointment.dto.room.request;

import com.medical.appointment.model.enums.RoomStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RoomRequest {
    @NotBlank(message = "Room number is required")
    @Size(max = 10)
    private String roomNumber;

    @NotBlank(message = "Room type is required")
    @Size(max = 20)
    private String roomType;

    @NotNull
    @Min(1)
    private Integer capacity;

    @NotNull
    private RoomStatus status;

    @Size(max = 255)
    private String equipmentAvailable;
}
