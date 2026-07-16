package com.medical.appointment.controller;

import com.medical.appointment.dto.admin.request.CreateAdminRequest;
import com.medical.appointment.dto.admin.response.AdminResponse;
import com.medical.appointment.dto.auth.request.LoginRequest;
import com.medical.appointment.dto.auth.request.VerificationRequest;
import com.medical.appointment.dto.auth.response.AuthResponse;
import com.medical.appointment.dto.doctor.request.DoctorRegisterRequest;
import com.medical.appointment.dto.doctor.response.DoctorResponse;
import com.medical.appointment.dto.patient.request.PatientRegisterRequest;
import com.medical.appointment.dto.patient.response.PatientResponse;
import com.medical.appointment.dto.staff.request.StaffRegisterRequest;
import com.medical.appointment.dto.staff.response.StaffResponse;
import com.medical.appointment.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.medical.appointment.dto.auth.request.ResendVerificationRequest;
import com.medical.appointment.dto.auth.request.ForgotPasswordRequest;
import com.medical.appointment.dto.auth.request.ResetPasswordRequest;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<PatientResponse> registerPatient(@Valid @RequestBody PatientRegisterRequest request) {
        PatientResponse response = authService.registerPatient(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/register/doctor")
    public ResponseEntity<DoctorResponse> registerDoctor(@Valid @RequestBody DoctorRegisterRequest request) {
        DoctorResponse response = authService.registerDoctor(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/register/staff")
    public ResponseEntity<StaffResponse> registerStaff(@Valid @RequestBody StaffRegisterRequest request) {
        StaffResponse response = authService.registerStaff(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/register/admin")
    public ResponseEntity<AdminResponse> registerAdmin(@Valid @RequestBody CreateAdminRequest request) {
        AdminResponse response = authService.registerAdmin(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verify(@Valid @RequestBody VerificationRequest request) {
        authService.verifyAccount(request);
        return ResponseEntity.ok("Account verified successfully! You can now login.");
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<String> resendVerificationCode(
            @Valid @RequestBody ResendVerificationRequest request
    ) {
        authService.resendVerificationCode(request);
        return ResponseEntity.ok("A new verification code has been sent to your email.");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {
        authService.forgotPassword(request);

        return ResponseEntity.ok(
                "If an active account exists with this email, a password reset code has been sent."
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        authService.resetPassword(request);

        return ResponseEntity.ok("Password has been reset successfully. You can now login.");
    }
}