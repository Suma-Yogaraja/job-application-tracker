package com.jobtracker.job_application_tracker.dto;


import com.jobtracker.job_application_tracker.model.InterviewRoundType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InterviewResponse {

    private Long id;
    private InterviewRoundType roundType;
    private LocalDateTime scheduledAt;
    private String notes;

}
