package com.jobtracker.job_application_tracker.dto;


import lombok.Data;
import org.apache.kafka.common.protocol.types.Field;

import java.time.LocalDateTime;

@Data
public class ApplicationCreatedEvent {

        private Long applicationId;
        private String company;
        private String role;
        private LocalDateTime createdAt;


}
