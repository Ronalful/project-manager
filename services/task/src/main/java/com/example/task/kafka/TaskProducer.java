package com.example.task.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskProducer {
    private final KafkaTemplate<String, TaskNotification> taskNotificationKafkaTemplate;
    private final KafkaTemplate<String, TaskChangedStateNotification> taskChangedStateNotificationKafkaTemplate;

    public void send(TaskNotification taskNotification) {
        taskNotificationKafkaTemplate.send("task-topic", taskNotification);
    }

    public void send(TaskChangedStateNotification taskChangedStateNotification) {
        taskChangedStateNotificationKafkaTemplate.send("task-changed-state-topic", taskChangedStateNotification);
    }
}
