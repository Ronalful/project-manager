package com.example.project.taskAssignment;

public record TaskAssignmentRequest(
        Integer taskId,
        Integer developerId
) {
}
