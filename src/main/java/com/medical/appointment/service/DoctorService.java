package com.medical.appointment.service;

import com.medical.appointment.dto.doctor.response.DoctorResponse;
import com.medical.appointment.model.Doctor;
import com.medical.appointment.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    @Transactional(readOnly = true)
    public List<DoctorResponse> getDoctorOptions() {
        return doctorRepository.findByUser_IsActiveTrue()
                .stream()
                .map(this::mapToDoctorResponse)
                .toList();
    }

    private DoctorResponse mapToDoctorResponse(Doctor doctor) {
        DoctorResponse response = new DoctorResponse();

        response.setUserId(doctor.getUser().getUserId());
        response.setEmail(doctor.getUser().getEmail());
        response.setFirstName(doctor.getUser().getFirstName());
        response.setLastName(doctor.getUser().getLastName());
        response.setPhone(doctor.getUser().getPhone());
        response.setNIC(doctor.getUser().getNIC());
        response.setAddress(doctor.getUser().getAddress());
        response.setIsActive(doctor.getUser().getIsActive());
        response.setRoleType(doctor.getUser().getRoleType());
        response.setCreatedAt(doctor.getUser().getCreatedAt());
        response.setUpdatedAt(doctor.getUser().getUpdatedAt());

        response.setDoctorId(doctor.getDoctorId());
        response.setSpecialization(doctor.getSpecialization());
        response.setLicenseNumber(doctor.getLicenseNumber());
        response.setQualification(doctor.getQualification());
        response.setExperienceYears(doctor.getExperienceYears());
        response.setConsultationFee(doctor.getConsultationFee());

        return response;
    }
}