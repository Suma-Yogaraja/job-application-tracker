package com.jobtracker.job_application_tracker.messaging;

import com.jobtracker.job_application_tracker.dto.InterviewScheduledEvent;
import com.jobtracker.job_application_tracker.dto.StatusChangedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class InterviewScheduledProducer {

    public static final String TOPIC="application-status-events";
    private final KafkaTemplate<String , InterviewScheduledEvent> kafkaTemplate;

    public InterviewScheduledProducer(KafkaTemplate<String, InterviewScheduledEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void publish(InterviewScheduledEvent event){
        kafkaTemplate.send(TOPIC,event.getApplicationId().toString(),event);
    }
}
