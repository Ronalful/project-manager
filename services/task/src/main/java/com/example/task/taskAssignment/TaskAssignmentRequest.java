package com.example.task.taskAssignment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record TaskAssignmentRequest(
        @Schema(
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "taskId is required")
        Integer taskId,

        @Schema(
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "developerId is required")
        Integer developerId
) {
}
