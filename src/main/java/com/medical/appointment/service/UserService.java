package com.medical.appointment.service;

import com.medical.appointment.dto.user.request.UserUpdateRequest;
import com.medical.appointment.dto.user.response.UserResponse;
import com.medical.appointment.model.User;
import com.medical.appointment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<UserResponse> getUserById(int id) {
        return userRepository.findById(id)
                .map(this::mapToUserResponse);
    }

    public Optional<UserResponse> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::mapToUserResponse);
    }

    public List<UserResponse> getActiveUsers() {
        return userRepository.findByIsActive(true)
                .stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    public List<UserResponse> getUsersByRole(int roleType) {
        return userRepository.findByRoleType(roleType)
                .stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    // Update
    public UserResponse updateUser(int id, UserUpdateRequest updateRequest) {
        return userRepository.findById(id).map(user -> {
            user.setFirstName(updateRequest.getFirstName());
            user.setLastName(updateRequest.getLastName());
            user.setPhone(updateRequest.getPhone());
            user.setAddress(updateRequest.getAddress());
            checkAuthorization(user.getEmail());

            User savedUser = userRepository.save(user);
            return mapToUserResponse(savedUser);
        }).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    // Update role

    public UserResponse updateUserRole(int id, int newRoleType) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!hasAnyRole("ROLE_SUPER_ADMIN", "ROLE_FULL")) {
            throw new AccessDeniedException("Insufficient permissions to modify user roles.");
        }

        user.setRoleType(newRoleType);
        return mapToUserResponse(userRepository.save(user));
    }

    // Activate user
    public void activateUser(int id){
        User user = userRepository.findById((id)).orElseThrow(() -> new RuntimeException("User not found"));
        checkAuthorization(user.getEmail());
        user.setIsActive(true);
        userRepository.save(user);
    }

    // Remove
    public void deactivateUser(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        checkAuthorization(user.getEmail());

        user.setIsActive(false);
        userRepository.save(user);
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

    private boolean hasAnyRole(String... roles) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;

        return auth.getAuthorities().stream()
                .anyMatch(a -> Arrays.asList(roles).contains(a.getAuthority()));
    }

    private void checkAuthorization(String targetUserEmail) {

        boolean canModifyOthers = hasAnyRole("ROLE_SUPER_ADMIN", "ROLE_FULL");
        boolean isOwner = isOwner(targetUserEmail);
        boolean isReadOnly = hasAnyRole("ROLE_READ_ONLY");

        if (isReadOnly) {
            throw new AccessDeniedException("Read-only access cannot modify data.");
        }

        if (canModifyOthers || isOwner) {
            return;
        }

        throw new AccessDeniedException("Unauthorized modification attempt.");
    }

    private boolean isOwner(String targetUserEmail) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getName().equals(targetUserEmail);
    }
}