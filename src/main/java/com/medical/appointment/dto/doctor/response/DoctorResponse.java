package com.medical.appointment.dto.doctor.response;

import com.medical.appointment.dto.user.response.UserResponse;
import com.medical.appointment.model.enums.Specialization;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DoctorResponse extends UserResponse {
    private Integer doctorId;
    private Specialization specialization;
    private String licenseNumber;
    private String qualification;
    private Integer experienceYears;
    private Double consultationFee;
}