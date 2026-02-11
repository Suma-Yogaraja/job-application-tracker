package com.jobtracker.job_application_tracker.messaging;


import com.jobtracker.job_application_tracker.dto.InterviewScheduledEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class InterviewScheduledConsumer {

    @KafkaListener(topics = InterviewScheduledProducer.TOPIC,groupId = "job-app-tracker")
    public void handle(InterviewScheduledEvent event){
        System.out.println("[EVENT] Interview scheduled: " +event);
    }
}
