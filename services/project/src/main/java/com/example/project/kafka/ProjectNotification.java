package com.example.project.kafka;

import com.example.project.user.UserResponse;

public record ProjectNotification(
        UserResponse user,
        String projectName,
        ProjectOperation operation
) {
}
