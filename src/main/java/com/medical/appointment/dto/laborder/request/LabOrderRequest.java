package com.medical.appointment.dto.laborder.request;

import com.medical.appointment.dto.laborderitem.request.LabOrderItemRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class LabOrderRequest {

    @NotNull(message = "Appointment ID is required")
    private Integer appointmentId;

    @NotNull(message = "Laboratory ID is required")
    private Integer laboratoryId;

    @NotEmpty(message = "At least one lab test must be ordered")
    @Valid
    private List<LabOrderItemRequest> items;
}
