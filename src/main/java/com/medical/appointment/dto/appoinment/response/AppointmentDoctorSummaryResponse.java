package com.medical.appointment.dto.appoinment.response;

import com.medical.appointment.model.enums.Specialization;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDoctorSummaryResponse {

    private Integer doctorId;
    private Integer userId;

    private String firstName;
    private String lastName;
    private String fullName;

    private String email;
    private String phone;

    private Specialization specialization;
    private String qualification;
    private Integer experienceYears;
    private Double consultationFee;
}