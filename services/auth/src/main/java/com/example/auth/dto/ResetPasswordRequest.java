package com.example.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запрос на смену пароля")
public record ResetPasswordRequest(
        @Schema(
                description = "Пароль пользователя",
                example = "securePassword123",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        String password
) {
}
