package com.jobtracker.job_application_tracker.controller;

import com.jobtracker.job_application_tracker.dto.AuthResponse;
import com.jobtracker.job_application_tracker.dto.LoginRequest;
import com.jobtracker.job_application_tracker.dto.RegisterRequest;
import com.jobtracker.job_application_tracker.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest){
        AuthResponse response=authService.register(registerRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid@RequestBody LoginRequest loginRequest){
         AuthResponse response=authService.login(loginRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
