package com.medical.appointment.repository;

import com.medical.appointment.model.LabOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LabOrderRepository extends JpaRepository<LabOrder, Integer> {
    Optional<LabOrder> findByAppointmentAppointmentId(Integer appointmentId);
    List<LabOrder> findByLaboratoryLaboratoryId(Integer laboratoryId);
    List<LabOrder> findByAppointmentPatientPatientId(Integer patientId);
    List<LabOrder> findByAppointmentDoctorDoctorId(Integer doctorId);
    boolean existsByAppointmentAppointmentId(Integer appointmentId);

    @Query("SELECT lo FROM LabOrder lo " +
            "JOIN lo.appointment a " +
            "JOIN lo.items i " +
            "WHERE (:patientId IS NULL OR a.patient.patientId = :patientId) " +
            "AND (:status IS NULL OR i.status = :status) " +
            "AND (:startDate IS NULL OR lo.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR lo.createdAt <= :endDate)")
    List<LabOrder> searchOrders(
            @Param("patientId") Integer patientId,
            @Param("status") String status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
