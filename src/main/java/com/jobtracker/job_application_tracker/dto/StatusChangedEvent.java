package com.jobtracker.job_application_tracker.dto;


import com.jobtracker.job_application_tracker.model.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StatusChangedEvent {

    private Long applicationId;
    private ApplicationStatus oldStatus;
    private ApplicationStatus newStatus;
    private LocalDateTime changedAt;

}
