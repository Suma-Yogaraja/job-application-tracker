package com.jobtracker.job_application_tracker.dto;

import com.jobtracker.job_application_tracker.model.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponse {

    private Long id;
    private String company;
    private String role;
    private ApplicationStatus status;
    private String notes;
    private String link;
    private LocalDate appliedDate;
}
