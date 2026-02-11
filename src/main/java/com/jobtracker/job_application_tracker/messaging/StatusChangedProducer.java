package com.jobtracker.job_application_tracker.messaging;

import com.jobtracker.job_application_tracker.dto.ApplicationResponse;
import com.jobtracker.job_application_tracker.dto.StatusChangedEvent;
import lombok.Data;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@ConditionalOnProperty(name = "app.kafka.enabled",havingValue = "true")
@Component
public class StatusChangedProducer {
    public static final String TOPIC="application-status-events";
    private final KafkaTemplate<String ,StatusChangedEvent> kafkaTemplate;


    public StatusChangedProducer(KafkaTemplate<String, StatusChangedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void  publish(StatusChangedEvent event){
        kafkaTemplate.send(TOPIC,event.getApplicationId().toString(),event);
    }
}
