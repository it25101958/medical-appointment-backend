package com.medical.appointment.service;

import com.medical.appointment.dto.room.request.RoomRequest;
import com.medical.appointment.dto.room.response.RoomResponse;
import com.medical.appointment.model.Room;
import com.medical.appointment.model.enums.AccessLevel;
import com.medical.appointment.model.enums.RoomStatus;
import com.medical.appointment.repository.RoomRepository;
import com.medical.appointment.security.SecurityAccessUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final SecurityAccessUtil securityAccessUtil;

    private void mapToEntity(RoomRequest request, Room room) {
        room.setRoomNumber(request.getRoomNumber());
        room.setRoomType(request.getRoomType());
        room.setCapacity(request.getCapacity());
        room.setStatus(request.getStatus());
        room.setEquipmentAvailable(request.getEquipmentAvailable());
    }


}
