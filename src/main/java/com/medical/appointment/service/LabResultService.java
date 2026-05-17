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
    @Transactional
    public LabResultResponse createLabResult(LabResultRequest request) {
        LabResult labResult = LabResult.builder()
                .appointmentId(request.getAppointmentId())
                .patientId(request.getPatientId())
                .testName(request.getTestName())
                .resultValue(request.getResultValue())
                .referenceRange(request.getReferenceRange())
                .status(request.getStatus())
                .remarks(request.getRemarks())
                .testDate(request.getTestDate())
                .build();

        LabResult savedResult = labResultRepository.save(labResult);
        return mapToResponse(savedResult);
    }

    @Transactional(readOnly = true)
    public LabResultResponse getLabResultById(Long id) {
        LabResult labResult = labResultRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lab result not found with id: " + id));
        return mapToResponse(labResult);
    }
    @Transactional(readOnly = true)
    public List<LabResultResponse> getLabResultsByPatientId(Long patientId) {
        return labResultRepository.findByPatientId(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public LabResultResponse updateLabResult(Long id, LabResultRequest request) {
        LabResult labResult = labResultRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lab result not found with id: " + id));

        labResult.setResultValue(request.getResultValue());
        labResult.setReferenceRange(request.getReferenceRange());
        labResult.setStatus(request.getStatus());
        labResult.setRemarks(request.getRemarks());
        if (request.getTestDate() != null) {
            labResult.setTestDate(request.getTestDate());
        }

        return mapToResponse(labResultRepository.save(labResult));
    }

}