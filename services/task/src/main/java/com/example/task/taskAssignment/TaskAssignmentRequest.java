package com.example.task.taskAssignment;

import jakarta.validation.constraints.NotNull;

public record TaskAssignmentRequest(
        @NotNull(message = "taskId is required")
        Integer taskId,
        @NotNull(message = "developerId is required")
        Integer developerId
) {
}
