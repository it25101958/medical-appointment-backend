package com.medical.appointment.dto.medication.response;

import com.medical.appointment.model.enums.MedicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicationResponse {
    private Integer medicationId;
    private String name;
    private String genericName;
    private String manufacturer;
    private String dosage;
    private String dosageForm;
    private MedicationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}