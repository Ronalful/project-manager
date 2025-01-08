package com.example.notification.kafka.task;

import com.example.notification.kafka.developer.Developer;
import com.example.notification.kafka.operation.Operation;
import com.example.notification.kafka.project.Project;

public record TaskNotification(
        Developer developer,
        String taskTitle,
        Project project,
        Operation operation
) {
}
