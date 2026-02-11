package com.jobtracker.job_application_tracker.messaging;


import com.jobtracker.job_application_tracker.dto.ApplicationCreatedEvent;
import com.jobtracker.job_application_tracker.dto.InterviewScheduledEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ApplicationCreatedProducer {
    public static final String TOPIC="application-created-events";
    private final KafkaTemplate<String , ApplicationCreatedEvent> kafkaTemplate;

    public ApplicationCreatedProducer(KafkaTemplate<String, ApplicationCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void publish(ApplicationCreatedEvent event){
        kafkaTemplate.send(TOPIC,event.getApplicationId().toString(),event);
    }
}
