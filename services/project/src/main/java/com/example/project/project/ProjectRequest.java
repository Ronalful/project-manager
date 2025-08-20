package com.example.project.project;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ProjectRequest(
        @Schema(
                description = "<strong>Название проекта</strong>",
                example = "ECM app",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Name is required")
        String name,

        @Schema(
                description = "<strong>Описание проекта</strong>",
                example = "description",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        String description
) {
}
