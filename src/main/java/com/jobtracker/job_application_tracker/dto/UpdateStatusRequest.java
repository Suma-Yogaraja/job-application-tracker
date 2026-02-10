package com.jobtracker.job_application_tracker.dto;


import com.jobtracker.job_application_tracker.model.ApplicationStatus;
import lombok.Data;

@Data
public  class UpdateStatusRequest {

    ApplicationStatus status;

}