package com.example.developer.developer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record DeveloperUpdateRequest(
        @Schema(
                description = "id",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Developer id is required")
        Integer id,

        @Schema(
                description = "Имя",
                example = "Ivan",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String firstname,

        @Schema(
                description = "Фамилия",
                example = "Ivanov",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String lastname,

        @Schema(
                description = "Почта",
                example = "IvanovIvan@gmail.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Email(message = "Developer email is not valid")
        String email
) {
}
