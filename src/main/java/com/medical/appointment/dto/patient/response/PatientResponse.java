package com.medical.appointment.dto.patient.response;

import com.medical.appointment.dto.user.response.UserResponse;
import com.medical.appointment.model.enums.BloodGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PatientResponse extends UserResponse {
    private Integer patientId;
    private String emergencyContact;
    private BloodGroup bloodGroup;
    private String allergies;
}