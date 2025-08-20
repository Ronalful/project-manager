package com.example.task.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record TaskRequest(
        @Schema(
                description = "<strong>id проекта</strong>",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Project id is required")
        Integer projectId,

        @Schema(
                description = "<strong>Название задачи</strong>",
                example = "Create UI",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Project id is required")
        String title,

        @Schema(
                description = "<strong>Описиние задачи</strong>",
                example = "description",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        String description,

        @Schema(
                description = "<strong>Приоритет задачи</strong>",
                example = "MEDIUM",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        TaskPriority priority,

        @Schema(
                description = "<strong>Статус задачи</strong>",
                example = "NOT_STARTED",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        TaskStatus status,

        @Schema(
                description = "<strong>Время дедлайна</strong>",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        Date dueDate
) {
}
