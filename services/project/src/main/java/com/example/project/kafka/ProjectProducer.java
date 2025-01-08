package com.example.project.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectProducer {

    private final KafkaTemplate<String, ProjectNotification> kafkaTemplate;

    public void send(ProjectNotification projectNotification) {
        kafkaTemplate.send("project-topic", projectNotification);
    }
}
