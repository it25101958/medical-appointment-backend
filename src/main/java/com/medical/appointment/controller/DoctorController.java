package com.medical.appointment.controller;

import com.medical.appointment.dto.doctor.response.DoctorResponse;
import com.medical.appointment.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping("/options")
    public ResponseEntity<List<DoctorResponse>> getDoctorOptions() {
        return ResponseEntity.ok(doctorService.getDoctorOptions());
    }
}