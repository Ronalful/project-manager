package com.example.project.projectAssignment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ProjectAssignmentRequest(
        @Schema(
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "projectId is required")
        Integer projectId,

        @Schema(
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotEmpty(message = "userId is required")
        List<Integer> userIds
) {
}
