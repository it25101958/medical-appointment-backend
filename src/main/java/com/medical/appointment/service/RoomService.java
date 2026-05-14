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

    private RoomResponse mapToResponse(Room room) {
        return new RoomResponse(
                room.getRoomId(),
                room.getRoomNumber(),
                room.getRoomType(),
                room.getCapacity(),
                room.getStatus(),
                room.getEquipmentAvailable()
        );
    }

    @Transactional
    public RoomResponse createRoom(RoomRequest request) {
        securityAccessUtil.validateAdminLevel(AccessLevel.FULL, AccessLevel.SUPER_ADMIN);

        Room room = new Room();
        mapToEntity(request, room);

        return mapToResponse(roomRepository.save(room));
    }
    @Transactional
    public RoomResponse updateRoom(Integer id, RoomRequest request) {
        securityAccessUtil.validateAdminLevel(AccessLevel.FULL, AccessLevel.SUPER_ADMIN);
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + id));

        mapToEntity(request, room);
        return mapToResponse(roomRepository.save(room));
    }

    @Transactional(readOnly = true)
    public List<RoomResponse> getAllRooms() {
        securityAccessUtil.validateAdminLevel(AccessLevel.FULL,AccessLevel.SUPER_ADMIN);
        return roomRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }
    @Transactional(readOnly = true)
    public RoomResponse getRoomById(Integer id) {
        return roomRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));
    }

    @Transactional
    public void deleteRoom(Integer id) {
        securityAccessUtil.validateAdminLevel(AccessLevel.SUPER_ADMIN);
        if (!roomRepository.existsById(id)) {
            throw new EntityNotFoundException("Room not found");
        }
        roomRepository.deleteById(id);
    }



}
