package com.example.task.task;

import com.example.task.developer.DeveloperResponse;
import com.example.task.project.ProjectResponse;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public record TaskResponse(
        Integer id,
        ProjectResponse project,
        String title,
        String description,
        TaskPriority priority,
        TaskStatus status,
        Date dueDate,
        List<DeveloperResponse> developers,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
