package com.medical.appointment.dto.laborder.response;

import com.medical.appointment.dto.laborderitem.response.LabOrderItemResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabOrderResponse {
    private Integer labOrderId;
    private Integer appointmentId;
    private String laboratoryName;
    private String patientName;
    private String doctorName;
    private List<LabOrderItemResponse> items;
}
