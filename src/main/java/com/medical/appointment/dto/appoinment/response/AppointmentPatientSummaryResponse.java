package com.medical.appointment.dto.appoinment.response;

import com.medical.appointment.model.enums.BloodGroup;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentPatientSummaryResponse {

    private Integer patientId;
    private Integer userId;

    private String firstName;
    private String lastName;
    private String fullName;

    private String email;
    private String phone;

    private BloodGroup bloodGroup;
    private String allergies;
}