package com.medical.appointment.service;

import com.medical.appointment.dto.laboratory.request.LaboratoryRequest;
import com.medical.appointment.dto.laboratory.response.LaboratoryResponse;
import com.medical.appointment.model.Laboratory;
import com.medical.appointment.model.enums.AccessLevel;
import com.medical.appointment.repository.LaboratoryRepository;
import com.medical.appointment.security.SecurityAccessUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LaboratoryService {

    private final LaboratoryRepository laboratoryRepository;
    private final SecurityAccessUtil securityAccessUtil;

    @Transactional
    public LaboratoryResponse createLaboratory(LaboratoryRequest request) {
        // Only Admins can register a new laboratory
        securityAccessUtil.validateAdminLevel(AccessLevel.FULL, AccessLevel.SUPER_ADMIN);

        if (laboratoryRepository.existsByName(request.getName())) {
            throw new IllegalStateException("A laboratory with this name already exists.");
        }

        Laboratory laboratory = new Laboratory();
        mapToEntity(request, laboratory);

        return mapToResponse(laboratoryRepository.save(laboratory));
    }

    @Transactional(readOnly = true)
    public List<LaboratoryResponse> getAllLaboratories() {
        return laboratoryRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LaboratoryResponse getLaboratoryById(Integer id) {
        return laboratoryRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Laboratory not found with ID: " + id));
    }

    @Transactional
    public LaboratoryResponse updateLaboratory(Integer id, LaboratoryRequest request) {
        securityAccessUtil.validateAdminLevel(AccessLevel.FULL, AccessLevel.SUPER_ADMIN);

        Laboratory laboratory = laboratoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Laboratory not found"));

        mapToEntity(request, laboratory);
        return mapToResponse(laboratoryRepository.save(laboratory));
    }

    @Transactional
    public void deleteLaboratory(Integer id) {
        securityAccessUtil.validateAdminLevel(AccessLevel.SUPER_ADMIN);
        if (!laboratoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Laboratory not found");
        }
        laboratoryRepository.deleteById(id);
    }

    // Helper Mappings
    private void mapToEntity(LaboratoryRequest request, Laboratory laboratory) {
        laboratory.setName(request.getName());
        laboratory.setAddress(request.getAddress());
        laboratory.setOpeningHours(request.getOpeningHours());
        laboratory.setPhone(request.getPhone());
        laboratory.setEmail(request.getEmail());
    }

    private LaboratoryResponse mapToResponse(Laboratory laboratory) {
        return new LaboratoryResponse(
                laboratory.getLaboratoryId(),
                laboratory.getName(),
                laboratory.getAddress(),
                laboratory.getOpeningHours(),
                laboratory.getPhone(),
                laboratory.getEmail(),
                laboratory.getCreatedAt(),
                laboratory.getUpdatedAt()
        );
    }
}