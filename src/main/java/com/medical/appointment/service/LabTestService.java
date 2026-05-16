package com.medical.appointment.service;

import com.medical.appointment.dto.labtest.request.LabTestRequest;
import com.medical.appointment.dto.labtest.response.LabTestResponse;
import com.medical.appointment.model.LabTest;
import com.medical.appointment.model.enums.AccessLevel;
import com.medical.appointment.repository.LabTestRepository;
import com.medical.appointment.security.SecurityAccessUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LabTestService {

    private final LabTestRepository labTestRepository;
    private final SecurityAccessUtil securityAccessUtil;

    @Transactional
    public LabTestResponse createLabTest(LabTestRequest request) {
        // Security: Allow Staff and High-level Admins to modify data
        securityAccessUtil.validateAdminLevel(AccessLevel.FULL, AccessLevel.SUPER_ADMIN);

        // Check for duplicate test names
        if (labTestRepository.findByTestName(request.getTestName()).isPresent()) {
            throw new IllegalStateException("A lab test with this name already exists.");
        }

        LabTest labTest = new LabTest();
        mapToEntity(request, labTest);

        return mapToResponse(labTestRepository.save(labTest));
    }

    @Transactional(readOnly = true)
    public List<LabTestResponse> getInactiveLabTests() {
        return labTestRepository.findByIsActive(false).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public LabTestResponse updateLabTest(Integer id, LabTestRequest request) {
        securityAccessUtil.validateAdminLevel(AccessLevel.FULL, AccessLevel.SUPER_ADMIN);

        LabTest existingTest = labTestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lab test not found with id: " + id));

        // Update fields
        mapToEntity(request, existingTest);

        return mapToResponse(labTestRepository.save(existingTest));
    }

    @Transactional(readOnly = true)
    public List<LabTestResponse> getAllLabTests() {
        return labTestRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LabTestResponse> getActiveLabTests() {
        // Filter by isActive status
        return labTestRepository.findByIsActive(true).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public LabTestResponse getLabTestById(Integer id) {
        return labTestRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Lab test not found"));
    }

    @Transactional
    public void deleteLabTest(Integer id) {
        // Restricted to higher authority for actual deletion
        securityAccessUtil.validateAdminLevel(AccessLevel.SUPER_ADMIN);

        if (!labTestRepository.existsById(id)) {
            throw new EntityNotFoundException("Lab test not found");
        }
        labTestRepository.deleteById(id);
    }

    // Helper methods for DTO Mapping
    private void mapToEntity(LabTestRequest request, LabTest entity) {
        entity.setTestName(request.getTestName());
        entity.setCategory(request.getCategory());
        entity.setDescription(request.getDescription());
        entity.setStandardPrice(request.getStandardPrice());
        entity.setIsActive(request.getIsActive());
    }

    private LabTestResponse mapToResponse(LabTest entity) {
        return new LabTestResponse(
                entity.getId(),
                entity.getTestName(),
                entity.getCategory(),
                entity.getDescription(),
                entity.getStandardPrice(),
                entity.getIsActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
