package com.medical.appointment.service;

import com.medical.appointment.dto.laborder.request.LabOrderRequest;
import com.medical.appointment.dto.laborder.response.LabOrderResponse;
import com.medical.appointment.dto.laborderitem.request.LabOrderItemRequest;
import com.medical.appointment.dto.laborderitem.response.LabOrderItemResponse;
import com.medical.appointment.model.*;
import com.medical.appointment.model.enums.AccessLevel;
import com.medical.appointment.repository.*;
import com.medical.appointment.security.SecurityAccessUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LabOrderService {

    private final LabOrderRepository labOrderRepository;
    private final AppointmentRepository appointmentRepository;
    private final LaboratoryRepository laboratoryRepository;
    private final LabTestRepository labTestRepository;
    private final SecurityAccessUtil securityAccessUtil;

    @Transactional
    public LabOrderResponse createLabOrder(LabOrderRequest request) {
        securityAccessUtil.validateDoctorAccess();
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        Laboratory laboratory = laboratoryRepository.findById(request.getLaboratoryId())
                .orElseThrow(() -> new EntityNotFoundException("Laboratory not found"));

        if (labOrderRepository.existsByAppointmentAppointmentId(request.getAppointmentId())) {
            throw new IllegalStateException("A lab order already exists for this appointment.");
        }
        LabOrder order = new LabOrder();
        order.setAppointment(appointment);
        order.setLaboratory(laboratory);
        List<LabOrderItem> items = request.getItems().stream().map(itemDto -> {
            LabTest test = labTestRepository.findById(itemDto.getLabTestId())
                    .orElseThrow(() -> new EntityNotFoundException("Lab Test not found: " + itemDto.getLabTestId()));

            return getLabOrderItem(order, itemDto, test);
        }).collect(Collectors.toList());
        order.setItems(items);

        return mapToResponse(labOrderRepository.save(order));
    }
    @Transactional(readOnly = true)
    public LabOrderResponse getLabOrderById(Integer id) {
        LabOrder order = labOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lab order not found"));
        securityAccessUtil.validateOwnership(order.getAppointment().getDoctor().getUser().getEmail());
        return mapToResponse(order);
    }

    @Transactional
    public LabOrderResponse updateLabOrder(Integer id, LabOrderRequest request) {
        securityAccessUtil.validateDoctorAccess();

        LabOrder order = labOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lab order not found"));

        boolean hasStarted = order.getItems().stream()
                .anyMatch(item -> !"PENDING".equals(item.getStatus()));
        if (hasStarted) {
            throw new IllegalStateException("Cannot modify order; some tests are already in progress or completed.");
        }

        if (!order.getLaboratory().getLaboratoryId().equals(request.getLaboratoryId())) {
            Laboratory lab = laboratoryRepository.findById(request.getLaboratoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Laboratory not found"));
            order.setLaboratory(lab);
        }

        order.getItems().clear();

        List<LabOrderItem> newItems = request.getItems().stream().map(itemDto -> {
            LabTest test = labTestRepository.findById(itemDto.getLabTestId())
                    .orElseThrow(() -> new EntityNotFoundException("Lab Test not found"));

            return getLabOrderItem(order, itemDto, test);
        }).toList();

        order.getItems().addAll(newItems);
        return mapToResponse(labOrderRepository.save(order));
    }
}
