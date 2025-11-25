package com.example.project.projectAssignment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ProjectAssignmentRequest(
        @NotNull(message = "projectId is required")
        Integer projectId,

        @NotEmpty(message = "userId is required")
        List<Integer> userIds
) {
}
