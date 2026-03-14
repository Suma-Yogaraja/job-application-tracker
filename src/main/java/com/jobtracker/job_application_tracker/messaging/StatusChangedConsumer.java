package com.jobtracker.job_application_tracker.messaging;


import com.jobtracker.job_application_tracker.dto.StatusChangedEvent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "true")
@Component
public class StatusChangedConsumer {
    @KafkaListener(topics = StatusChangedProducer.TOPIC,groupId = "job-app-tracker")
    public void handle(StatusChangedEvent event){
        System.out.println("[EVENT] " + event);
    }
}
