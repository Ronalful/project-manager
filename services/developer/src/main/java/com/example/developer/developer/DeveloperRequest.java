package com.example.developer.developer;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record DeveloperRequest(
        @Schema(
                description = "<strong>Имя</strong>",
                example = "Ivan",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Developer firstname is required")
        String firstname,

        @Schema(
                description = "<strong>Фамилия</strong>",
                example = "Ivanov",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Developer lastname is required")
        String lastname,

        @Schema(
                description = "<strong>Почта</strong>",
                example = "IvanovIvan@gmail.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Developer email is required")
        @Email(message = "Developer email is not valid")
        String email
) {
}
