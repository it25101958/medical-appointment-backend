package com.medical.appointment.controller;

import com.medical.appointment.dto.admin.request.CreateAdminRequest;
import com.medical.appointment.dto.auth.request.LoginRequest;
import com.medical.appointment.dto.auth.request.VerificationRequest;
import com.medical.appointment.dto.auth.response.AuthResponse;
import com.medical.appointment.dto.doctor.request.DoctorRegisterRequest;
import com.medical.appointment.dto.patient.request.PatientRegisterRequest;
import com.medical.appointment.dto.staff.request.StaffRegisterRequest;
import com.medical.appointment.model.User;
import com.medical.appointment.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerPatient(@Valid @RequestBody PatientRegisterRequest request) {
        User savedUser = authService.registerPatient(request);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/register/doctor")
    public ResponseEntity<User> registerDoctor(@Valid @RequestBody DoctorRegisterRequest request) {
        User savedUser = authService.registerDoctor(request);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/register/staff")
    public ResponseEntity<User> registerStaff(@Valid @RequestBody StaffRegisterRequest request) {
        User savedUser = authService.registerStaff(request);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/register/admin")
    public ResponseEntity<User> registerAdmin(@Valid @RequestBody CreateAdminRequest request) {
        User savedUser = authService.registerAdmin(request);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verify(@Valid @RequestBody VerificationRequest request) {
        authService.verifyAccount(request);
        return ResponseEntity.ok("Account verified successfully! You can now login.");
    }
}