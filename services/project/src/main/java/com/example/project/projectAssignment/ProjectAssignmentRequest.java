package com.example.project.projectAssignment;

import jakarta.validation.constraints.NotNull;

public record ProjectAssignmentRequest(
        @NotNull(message = "projectId is required")
        Integer projectId,
        @NotNull(message = "developerId is required")
        Integer developerId
) {
}
