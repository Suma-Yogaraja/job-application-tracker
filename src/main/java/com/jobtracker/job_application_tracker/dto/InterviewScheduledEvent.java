package com.jobtracker.job_application_tracker.dto;

import com.jobtracker.job_application_tracker.model.InterviewRoundType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InterviewScheduledEvent {

    private Long interviewId;
    private Long applicationId;
    private InterviewRoundType roundType;
    private LocalDateTime scheduledAt;
    private LocalDateTime createdAt;

}
