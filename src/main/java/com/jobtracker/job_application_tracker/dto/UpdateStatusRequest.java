package com.jobtracker.job_application_tracker.dto;


import com.jobtracker.job_application_tracker.model.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public  class UpdateStatusRequest {

    @NotNull
    ApplicationStatus status;

}