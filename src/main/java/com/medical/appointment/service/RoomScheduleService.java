package com.medical.appointment.service;

import com.medical.appointment.dto.roomschedule.request.RoomScheduleRequest;
import com.medical.appointment.dto.roomschedule.response.RoomScheduleResponse;
import com.medical.appointment.model.*;
import com.medical.appointment.model.enums.AccessLevel;
import com.medical.appointment.model.enums.DayOfWeek;
import com.medical.appointment.model.enums.RoomStatus;
import com.medical.appointment.repository.*;
import com.medical.appointment.security.SecurityAccessUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomScheduleService {

    private final RoomScheduleRepository roomScheduleRepository;
    private final RoomRepository roomRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final SecurityAccessUtil securityAccessUtil;
}
