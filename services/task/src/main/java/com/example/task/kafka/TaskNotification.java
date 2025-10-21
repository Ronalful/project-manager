package com.example.task.kafka;

import com.example.task.developer.UserResponse;
import com.example.task.project.ProjectResponse;

public record TaskNotification(
        UserResponse developer,
        String taskTitle,
        ProjectResponse project,
        TaskOperation operation
) {
}
