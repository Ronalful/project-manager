package com.example.project.project;

import jakarta.validation.constraints.NotNull;

public record ProjectRequest(
        @NotNull(message = "Name is required")
        String name,
        String description
) {
}
