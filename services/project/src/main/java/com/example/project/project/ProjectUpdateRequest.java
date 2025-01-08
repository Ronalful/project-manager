package com.example.project.project;

import jakarta.validation.constraints.NotNull;

public record ProjectUpdateRequest(
        @NotNull(message = "Project id is required")
        Integer id,
        String name,
        String description
) {
}

