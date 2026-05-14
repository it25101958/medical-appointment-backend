package com.medical.appointment.controller;

import com.medical.appointment.dto.user.request.UserUpdateRequest;
import com.medical.appointment.dto.user.response.UserResponse;
import com.medical.appointment.model.User;
import com.medical.appointment.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        Optional<UserResponse> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/active")
    public List<UserResponse> getActiveUsers() {
        return userService.getActiveUsers();
    }

    @GetMapping("/role/{roleType}")
    public List<UserResponse> getUsersByRole(@PathVariable int roleType) {
        return userService.getUsersByRole(roleType);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable int id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // update user
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable int id,
            @Valid @RequestBody UserUpdateRequest request
    ) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @PatchMapping("/activate/{id}")
    public ResponseEntity<Void> activateUser(@PathVariable int id){
        userService.activateUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/deactivate/{id}")
    public ResponseEntity<Void> deactivateUser(@PathVariable int id) {
        userService.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/role/{id}")
    public ResponseEntity<UserResponse> updateUserRole(
            @PathVariable int id,
            @RequestParam int newRoleType) {

        return ResponseEntity.ok(userService.updateUserRole(id, newRoleType));
    }

}