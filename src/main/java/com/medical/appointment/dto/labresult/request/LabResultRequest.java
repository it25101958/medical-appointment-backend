package com.medical.appointment.dto.labresult.request;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class LabResultRequest {
    private Long appointmentId;
    private Long patientId;
    private Long labTestId;
    private String testName;
    private String resultValue;
    private String referenceRange;
    private String status;
    private String remarks;
    private LocalDate testDate;
}