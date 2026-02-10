package com.jobtracker.job_application_tracker.controller;

import com.jobtracker.job_application_tracker.dto.ApplicationResponse;
import com.jobtracker.job_application_tracker.dto.CreateApplicationRequest;
import com.jobtracker.job_application_tracker.dto.CreateInterviewRequest;
import com.jobtracker.job_application_tracker.dto.InterviewResponse;
import com.jobtracker.job_application_tracker.service.InterviewService;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications/{applicationID}/interviews")
public class InterviewController {

    private final  InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping
    public InterviewResponse create(@PathVariable Long applicationID,
                                                   @ Valid @RequestBody CreateInterviewRequest request) {

        // This gets the email from the JWT you sent in the Header
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return interviewService.createInterview(applicationID,request,email);
    }

    @GetMapping
    public List<InterviewResponse> listInterview(@PathVariable Long applicationID){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return interviewService.listInterviews(applicationID,email);
    }
}
