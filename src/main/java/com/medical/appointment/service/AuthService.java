package com.medical.appointment.service;

import com.medical.appointment.dto.AuthResponse;
import com.medical.appointment.dto.LoginRequest;
import com.medical.appointment.exception.InvalidCredentialsException;
import com.medical.appointment.exception.UserAlreadyExistsException;
import com.medical.appointment.exception.UserNotFoundException;
import com.medical.appointment.model.User;
import com.medical.appointment.model.enums.UserRole;
import com.medical.appointment.repository.UserRepository;
import com.medical.appointment.security.JwtTokenUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + loginRequest.getEmail()));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtTokenUtil.generateToken(user.getEmail(), String.valueOf(user.getRoleType()));
        return new AuthResponse(token, user);
    }

    public User register(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            System.out.println("Authenticated: " + auth.isAuthenticated());
            System.out.println("Principal: " + auth.getPrincipal());
            System.out.println("Authorities: " + auth.getAuthorities());
        }

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            if (user.getRoleType() != UserRole.PATIENT.getValue()) {
                throw new IllegalArgumentException("Guest users can only register as Patients.");
            }
            return userRepository.save(user);
        }

        String currentUsername = auth.getName();
        User loggedInUser = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

        int loggedInRole = loggedInUser.getRoleType();
        int targetRole = user.getRoleType();
        if (loggedInRole == UserRole.SUPER_ADMIN.getValue()) {
            if (targetRole == UserRole.PATIENT.getValue()) {
                throw new IllegalArgumentException("Super Admin cannot create a patient account.");
            }
        } else if (loggedInRole == UserRole.ADMIN.getValue()) {
            if (targetRole != UserRole.DOCTOR.getValue() && targetRole != UserRole.STAFF.getValue()) {
                throw new IllegalArgumentException("Admin can only create Doctor or Staff accounts.");
            }
        } else {
            if (targetRole != UserRole.PATIENT.getValue()) {
                throw new IllegalArgumentException("Patients cannot create other accounts.");
            }
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

}