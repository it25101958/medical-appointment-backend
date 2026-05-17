package com.medical.appointment.service;

import com.medical.appointment.dto.labresult.request.LabResultRequest;
import com.medical.appointment.dto.labresult.response.LabResultResponse;
import com.medical.appointment.model.LabResult;
import com.medical.appointment.repository.LabResultRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LabResultService {

    private final LabResultRepository labResultRepository;

    private LabResultResponse mapToResponse(LabResult labResult) {
        LabResultResponse response = new LabResultResponse();
        response.setId(labResult.getId());
        response.setAppointmentId(labResult.getAppointmentId());
        response.setPatientId(labResult.getPatientId());
        response.setTestName(labResult.getTestName());
        response.setResultValue(labResult.getResultValue());
        response.setReferenceRange(labResult.getReferenceRange());
        response.setStatus(labResult.setStatus());
        response.setRemarks(labResult.getRemarks());
        response.setCreatedAt(labResult.getCreatedAt());
        response.setUpdatedAt(labResult.getUpdatedAt());
        return response;
    }

    @Transactional(readOnly = true)
    public LabResultResponse getLabResultById(Long id) {
        LabResult labResult = labResultRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lab result not found with id: " + id));
        return mapToResponse(labResult);
    }

}