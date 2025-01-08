package com.example.notification.kafka.task;

import com.example.notification.kafka.developer.Developer;
import com.example.notification.kafka.project.Project;

public record TaskChangedStateNotification(
        Developer developer,
        String taskTitle,
        TaskStatus taskStatus,
        Project project
) {
}
