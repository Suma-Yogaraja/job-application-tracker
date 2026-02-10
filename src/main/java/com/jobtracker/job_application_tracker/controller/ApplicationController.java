package com.jobtracker.job_application_tracker.controller;

import com.jobtracker.job_application_tracker.dto.ApplicationResponse;
import com.jobtracker.job_application_tracker.dto.CreateApplicationRequest;
import com.jobtracker.job_application_tracker.dto.UpdateStatusRequest;
import com.jobtracker.job_application_tracker.model.Application;
import com.jobtracker.job_application_tracker.model.ApplicationStatus;
import com.jobtracker.job_application_tracker.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")

public class ApplicationController {


    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public ResponseEntity<ApplicationResponse> create(@RequestBody CreateApplicationRequest request) {

        System.out.println("POSTMAN DATA: " + request.getCompany());

        // This gets the email from the JWT you sent in the Header
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(applicationService.create(request, email));
    }

    @GetMapping
    public ResponseEntity<List<ApplicationResponse>> getMyApplications() {

        // This ensures User A only sees User A data
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(applicationService.getMyApplications(email));
    }

    @PostMapping("/{applicationID}/status")
    public ResponseEntity<ApplicationResponse> updateStatus(@PathVariable Long applicationID
                                                                                                                , @Valid @RequestBody ApplicationStatus req){


        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(applicationService.updateStatus(applicationID,req,email));
    }
}