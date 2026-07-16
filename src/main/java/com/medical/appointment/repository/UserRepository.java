package com.medical.appointment.repository;

import com.medical.appointment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByNIC(String NIC);
    Optional<User> findByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByNIC(String NIC);
    List<User> findByIsActive(Boolean isActive);
    List<User> findByRoleType(int roleType);
}