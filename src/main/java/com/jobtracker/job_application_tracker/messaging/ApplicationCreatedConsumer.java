package com.jobtracker.job_application_tracker.messaging;


import com.jobtracker.job_application_tracker.dto.ApplicationCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationCreatedConsumer {

    @KafkaListener(topics = ApplicationCreatedProducer.TOPIC,groupId = "job-app-tracker")
    public void handle(ApplicationCreatedEvent event){
        System.out.println("[EVENT] Application Created :" +event);
    }
}
