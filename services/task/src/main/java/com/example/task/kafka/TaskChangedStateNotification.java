package com.example.task.kafka;

import com.example.task.developer.DeveloperResponse;
import com.example.task.project.ProjectResponse;
import com.example.task.task.TaskStatus;

public record TaskChangedStateNotification(
        DeveloperResponse developer,
        String taskTitle,
        TaskStatus taskStatus,
        ProjectResponse project
) {
}
