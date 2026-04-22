package com.medical.appointment.repository;

import com.medical.appointment.model.Doctor;
import com.medical.appointment.model.enums.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    Optional<Doctor> findByUserUserId(Integer userId);
    Optional<Doctor> findByLicenseNumber(String licenseNumber);
    List<Doctor> findBySpecialization(Specialization specialization);
    List<Doctor> findByExperienceYearsGreaterThanEqual(Integer years);
    List<Doctor> findByConsultationFeeLessThanEqual(Double maxFee);
}