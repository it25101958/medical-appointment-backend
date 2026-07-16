package com.medical.appointment.dto.labresult.response;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class LabResultResponse {
    private Long id;
    private Long appointmentId;
    private Long patientId;
    private String patientName;
    private String testName;
    private String resultValue;
    private String referenceRange;
    private String status;
    private String remarks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}