package com.medical.appointment.dto.room.response;

import com.medical.appointment.model.enums.RoomStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomResponse {
    private Integer roomId;
    private String roomNumber;
    private String roomType;
    private Integer capacity;
    private RoomStatus status;
    private String equipmentAvailable;
}
