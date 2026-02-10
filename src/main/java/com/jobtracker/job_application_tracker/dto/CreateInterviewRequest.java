package com.jobtracker.job_application_tracker.dto;


import com.jobtracker.job_application_tracker.model.InterviewRoundType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateInterviewRequest {

    @NotNull
    private InterviewRoundType roundType;

    @NotNull
    private LocalDateTime scheduledAt;

    private String notes;

}
