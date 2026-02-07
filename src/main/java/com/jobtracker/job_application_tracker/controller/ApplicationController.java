package com.jobtracker.job_application_tracker.controller;

import com.jobtracker.job_application_tracker.dto.CreateApplicationRequest;
import com.jobtracker.job_application_tracker.model.Application;
import com.jobtracker.job_application_tracker.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<Application> create(@RequestBody CreateApplicationRequest request) {

        System.out.println("POSTMAN DATA: " + request.getCompany());

        // This gets the email from the JWT you sent in the Header
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(applicationService.create(request, email));
    }

    @GetMapping
    public ResponseEntity<List<Application>> getMyApplications() {

        // This ensures User A only sees User A data
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(applicationService.getMyApplication(email));
    }
}