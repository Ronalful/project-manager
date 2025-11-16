package com.example.notification.kafka.project;

import com.example.notification.kafka.developer.UserResponse;
import com.example.notification.kafka.operation.Operation;

public record ProjectNotification(
        UserResponse user,
        String projectName,
        Operation operation
) {
}
