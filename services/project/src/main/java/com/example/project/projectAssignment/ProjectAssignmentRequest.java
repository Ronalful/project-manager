package com.example.project.projectAssignment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

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
        @NotNull(message = "developerId is required")
        Integer developerId
) {
}
