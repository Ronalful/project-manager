package com.example.auth.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запрос на активацию пользователя")
public record ActivationRequest(
        @Schema(
                description = "Секретная фраза пользователя",
                example = "Тортик",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        String secretPhrase,

        @Schema(
                description = "Пароль пользователя",
                example = "securePassword123",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        String password
) {
}
