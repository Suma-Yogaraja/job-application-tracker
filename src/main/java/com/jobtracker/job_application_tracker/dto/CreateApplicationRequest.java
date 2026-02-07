package com.jobtracker.job_application_tracker.dto;

import com.jobtracker.job_application_tracker.model.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateApplicationRequest {

    @NotBlank
    private String company;

    @NotBlank
    private String role;

    @NotNull
    private ApplicationStatus status;

    private String notes;
    private String link;

    @PastOrPresent
    private LocalDate appliedDate;
}
