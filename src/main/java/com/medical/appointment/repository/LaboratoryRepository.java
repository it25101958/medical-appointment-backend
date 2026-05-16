package com.medical.appointment.repository;

import com.medical.appointment.model.Laboratory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LaboratoryRepository extends JpaRepository<Laboratory, Integer> {
    Optional<Laboratory> findByEmail(String email);
    boolean existsByName(String name);
}