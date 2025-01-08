package com.example.notification.kafka.project;

import com.example.notification.kafka.developer.Developer;
import com.example.notification.kafka.operation.Operation;

public record ProjectNotification(
        Developer developer,
        String projectName,
        Operation operation
) {
}
