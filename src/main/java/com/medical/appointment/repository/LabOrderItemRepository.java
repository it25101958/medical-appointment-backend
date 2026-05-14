package com.medical.appointment.repository;

import com.medical.appointment.model.LabOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabOrderItemRepository extends JpaRepository<LabOrderItem, Integer> {
    List<LabOrderItem> findByLabOrderItemId(Integer labOrderItemId);
    List<LabOrderItem> findByStatus(String status);
    List<LabOrderItem> findByLabTestId(Integer testId);
}
