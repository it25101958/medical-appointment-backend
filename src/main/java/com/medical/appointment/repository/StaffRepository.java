package com.medical.appointment.repository;

import com.medical.appointment.model.Staff;
import com.medical.appointment.model.enums.StaffStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
    Optional<Staff> findByUserEmail(String email);
    List<Staff> findByStatus(StaffStatus status);
    List<Staff> findBySpecializationContainingIgnoreCase(String specialization);
}