package com.example.task.task;

import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record TaskUpdateRequest(
        @NotNull
        Integer id,
        String title,
        String description,
        TaskPriority priority,
        TaskStatus status,
        Date dueDate
) {
}
