package com.example.notification.kafka.task;

import com.example.notification.kafka.developer.UserResponse;
import com.example.notification.kafka.operation.Operation;
import com.example.notification.kafka.project.Project;

public record TaskNotification(
        UserResponse userResponse,
        String taskTitle,
        Project project,
        Operation operation
) {
}
