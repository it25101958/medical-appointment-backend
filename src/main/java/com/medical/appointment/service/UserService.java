package com.medical.appointment.service;

import com.medical.appointment.dto.user.response.UserResponse;
import com.medical.appointment.model.User;
import com.medical.appointment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<UserResponse> getUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.map(user -> new UserResponse(
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
        ));
    }

    public List<User> getActiveUsers() {
        return userRepository.findByIsActive(true);
    }

    public List<User> getUsersByRole(int roleType) {
        return userRepository.findByRoleType(roleType);
    }
}