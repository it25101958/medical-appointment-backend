package com.medical.appointment.repository;

import com.medical.appointment.model.Patient;
import com.medical.appointment.model.enums.BloodGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Optional<Patient> findByUserUserId(Integer userId);
    Optional<Patient> findByUserEmail(String email);
    List<Patient> findByBloodGroup(BloodGroup bloodGroup);
    Optional<Patient> findByEmergencyContact(String emergencyContact);
}