
package com.almabridge.controller;

import com.almabridge.dto.auth.*;
import com.almabridge.security.CustomUserDetails;
import com.almabridge.service.AuthService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/student")
    public ResponseEntity<RegistrationResponse> registerStudent(
            @Valid @RequestBody StudentRegistrationRequest request) {

        RegistrationResponse response =
                authService.registerStudent(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/register/alumni")
    public ResponseEntity<RegistrationResponse> registerAlumni(
            @Valid @RequestBody AlumniRegistrationRequest request) {

        RegistrationResponse response =
                authService.registerAlumni(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse response =
                authService.login(
                        request.email(),
                        request.password()
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUser(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserProfileResponse response =
                authService.getCurrentUser(userDetails.getUserId());

        return ResponseEntity.ok(response);
    }
}
