package com.medical.appointment.repository;

import com.medical.appointment.model.PrescriptionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface PrescriptionItemRepository extends JpaRepository<PrescriptionItem, Integer> {
    List<PrescriptionItem> findByPrescriptionPrescriptionId(Integer prescriptionId);
    List<PrescriptionItem> findByMedicationMedicationId(Integer medicationId);
    void deleteByPrescriptionPrescriptionId(Integer prescriptionId);
    Page<PrescriptionItem> findByPrescriptionPrescriptionId(Integer prescriptionId, Pageable pageable);
}
