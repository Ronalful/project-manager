package com.example.notification.kafka.task;

import com.example.notification.kafka.developer.UserResponse;
import com.example.notification.kafka.project.Project;

public record TaskChangedStateNotification(
        UserResponse userResponse,
        String taskTitle,
        TaskStatus taskStatus,
        Project project
) {
}
