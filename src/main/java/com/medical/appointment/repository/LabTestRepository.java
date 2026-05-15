package com.medical.appointment.repository;

import com.medical.appointment.model.LabTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabTestRepository extends JpaRepository<LabTest, Integer> {
    Optional<LabTest> findByTestName(String testName);
    List<LabTest> findByIsActive(Boolean isActive);
    List<LabTest> findByCategoryIgnoreCase(String category);
}

