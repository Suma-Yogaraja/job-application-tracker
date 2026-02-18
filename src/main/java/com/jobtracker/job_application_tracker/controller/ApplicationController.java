package com.jobtracker.job_application_tracker.controller;

import com.jobtracker.job_application_tracker.dto.ApplicationResponse;
import com.jobtracker.job_application_tracker.dto.CreateApplicationRequest;
import com.jobtracker.job_application_tracker.dto.PagedResponse;
import com.jobtracker.job_application_tracker.dto.UpdateStatusRequest;
import com.jobtracker.job_application_tracker.model.Application;
import com.jobtracker.job_application_tracker.model.ApplicationStatus;
import com.jobtracker.job_application_tracker.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.data.domain.Page;
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

    @PatchMapping("/{applicationID}/status")
    public ResponseEntity<ApplicationResponse> updateStatus(@PathVariable Long applicationID
                                                                                                                , @Valid @RequestBody UpdateStatusRequest req){


        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(applicationService.updateStatus(applicationID,req.getStatus(),email));
    }
    @GetMapping("/paged")
    public ResponseEntity<PagedResponse<ApplicationResponse>>  getApplicationPaged(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
            ){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Page<ApplicationResponse> result =
                applicationService.getMyApplicationsPaged(email, status, company, role, page, size, sort);

        PagedResponse<ApplicationResponse> response = new PagedResponse<>(
                result.getContent(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isLast()
        );

        return ResponseEntity.ok(response);
    }
}