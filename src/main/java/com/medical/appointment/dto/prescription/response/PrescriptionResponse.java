package com.medical.appointment.dto.prescription.response;

import com.medical.appointment.model.enums.PrescriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.medical.appointment.dto.prescriptionItem.response.PrescriptionItemResponse;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionResponse {

    private Integer prescriptionId;
    private Integer appointmentId;
    private String doctorName;
    private String patientName;
    private LocalDate prescriptionDate;
    private PrescriptionStatus status;
    private String notes;
    private List<PrescriptionItemResponse> items;
    private LocalDateTime createdAt;
}