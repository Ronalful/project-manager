package com.example.task.task;

import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record TaskRequest(
        @NotNull(message = "Project id is required")
        Integer projectId,
        @NotNull(message = "Project id is required")
        String title,
        String description,
        TaskPriority priority,
        TaskStatus status,
        Date dueDate
) {
}
