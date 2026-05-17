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

}