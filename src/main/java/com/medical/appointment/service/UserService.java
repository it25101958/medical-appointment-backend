package com.medical.appointment.service;

import com.medical.appointment.dto.user.request.UserUpdateRequest;
import com.medical.appointment.dto.user.response.UserResponse;
import com.medical.appointment.model.User;
import com.medical.appointment.model.enums.AccessLevel;
import com.medical.appointment.repository.UserRepository;
import com.medical.appointment.security.SecurityAccessUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SecurityAccessUtil securityAccessUtil;

    public UserService(UserRepository userRepository, SecurityAccessUtil securityAccessUtil) {
        this.userRepository = userRepository;
        this.securityAccessUtil = securityAccessUtil;
    }

    @Transactional(readOnly = true)
    public UserResponse getMyProfile() {
        String currentEmail = securityAccessUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated user not found"));

        return mapToUserResponse(user);
    }

    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserById(int id) {
        return userRepository.findById(id)
                .map(user -> {
                    validateUserReadAccess(user);
                    return mapToUserResponse(user);
                });
    }

    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserByEmail(String email) {
        validateAdminReadAccess();

        return userRepository.findByEmail(email)
                .map(this::mapToUserResponse);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getActiveUsers() {
        validateAdminReadAccess();

        return userRepository.findByIsActive(true)
                .stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        validateAdminReadAccess();

        return userRepository.findAll(pageable)
                .map(this::mapToUserResponse);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByRole(int roleType) {
        validateAdminReadAccess();

        return userRepository.findByRoleType(roleType)
                .stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    @Transactional
    public UserResponse updateUser(int id, UserUpdateRequest updateRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        securityAccessUtil.validateModificationAccess(user.getEmail());

        user.setFirstName(updateRequest.getFirstName());
        user.setLastName(updateRequest.getLastName());
        user.setPhone(updateRequest.getPhone());
        user.setAddress(updateRequest.getAddress());

        User savedUser = userRepository.save(user);
        return mapToUserResponse(savedUser);
    }

    @Transactional
    public UserResponse updateUserRole(int id, int newRoleType) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        securityAccessUtil.validateAdminLevel(AccessLevel.SUPER_ADMIN);

        user.setRoleType(newRoleType);
        return mapToUserResponse(userRepository.save(user));
    }

    @Transactional
    public void activateUser(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        securityAccessUtil.validateAdminLevel(AccessLevel.SUPER_ADMIN, AccessLevel.FULL);

        user.setIsActive(true);
        userRepository.save(user);
    }

    @Transactional
    public void deactivateUser(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        securityAccessUtil.validateAdminLevel(AccessLevel.SUPER_ADMIN, AccessLevel.FULL);

        user.setIsActive(false);
        userRepository.save(user);
    }

    private void validateUserReadAccess(User user) {
        if (securityAccessUtil.isOwner(user.getEmail())) {
            return;
        }

        validateAdminReadAccess();
    }

    private void validateAdminReadAccess() {
        securityAccessUtil.validateAdminLevel(
                AccessLevel.READ_ONLY,
                AccessLevel.LIMITED,
                AccessLevel.FULL,
                AccessLevel.SUPER_ADMIN
        );
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getUserId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getNIC(),
                user.getAddress(),
                user.getIsActive(),
                user.getRoleType(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}