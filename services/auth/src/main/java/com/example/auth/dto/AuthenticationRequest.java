package com.example.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запрос на аутентификацию пользователя")
public record AuthenticationRequest(
        @Schema(
                description = "Email пользователя",
                example = "user@example.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        @Email
        String email,

        @Schema(
                description = "Пароль пользователя",
                example = "password123",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        String password
) {
}
