package com.example.project.kafka;

import com.example.project.developer.DeveloperResponse;

public record ProjectNotification(
        DeveloperResponse developer,
        String projectName,
        ProjectOperation operation
) {
}
