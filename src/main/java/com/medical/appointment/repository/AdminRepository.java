package com.medical.appointment.repository;

import com.medical.appointment.model.Admin;
import com.medical.appointment.model.enums.AccessLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByUserUserId(Integer userId);
    Optional<Admin> findByUserEmail(String email);
    List<Admin> findByAccessLevel(AccessLevel accessLevel);
    List<Admin> findByDepartmentIgnoreCase(String department);
}