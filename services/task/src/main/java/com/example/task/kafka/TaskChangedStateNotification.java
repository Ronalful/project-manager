package com.example.task.kafka;

import com.example.task.developer.UserResponse;
import com.example.task.project.ProjectResponse;
import com.example.task.task.TaskStatus;

public record TaskChangedStateNotification(
        UserResponse developer,
        String taskTitle,
        TaskStatus taskStatus,
        ProjectResponse project
) {
}
