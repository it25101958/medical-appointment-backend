package com.medical.appointment.dto.staff.response;

import com.medical.appointment.dto.user.response.UserResponse;
import com.medical.appointment.model.enums.StaffStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StaffResponse extends UserResponse {
    private Integer staffId;
    private StaffStatus status;
    private String workingHours;
    private String specialization;
}