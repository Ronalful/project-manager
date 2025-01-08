package com.example.task.kafka;

import com.example.task.developer.DeveloperResponse;
import com.example.task.project.ProjectResponse;

public record TaskNotification(
        DeveloperResponse developer,
        String taskTitle,
        ProjectResponse project,
        TaskOperation operation
) {
}
