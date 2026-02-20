package com.jobtracker.job_application_tracker.messaging;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobtracker.job_application_tracker.dto.InterviewScheduledEvent;
import com.jobtracker.job_application_tracker.model.ActivityLog;
import com.jobtracker.job_application_tracker.repository.ActivityLogRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class InterviewScheduledConsumer {
    private final ActivityLogRepository activityLogRepository;
    private final ObjectMapper objectMapper;

    public InterviewScheduledConsumer(ActivityLogRepository activityLogRepository, ObjectMapper objectMapper) {
        this.activityLogRepository = activityLogRepository;
        this.objectMapper = objectMapper;
    }


    @KafkaListener(topics = InterviewScheduledProducer.TOPIC,groupId = "job-app-tracker")
    public void handle(InterviewScheduledEvent event) throws Exception{

        String payloadJson = objectMapper.writeValueAsString(event);

        ActivityLog log = new ActivityLog();
        log.setEventType("InterviewScheduledEvent");
        log.setInterviewId(event.getInterviewId());
        log.setApplicationId(event.getApplicationId());
        log.setPayload(payloadJson);

        log.setEntityType("INTERVIEW");
        log.setEntityId(event.getInterviewId());

        activityLogRepository.save(log);
        System.out.println("[EVENT] Interview scheduled: " +event);
    }
}
