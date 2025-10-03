package com.example.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запрос на подтверждения секретного слова для сброса пароля")
public record InitiateResetPasswordRequest(
        @Schema(
                description = "Email пользователя",
                example = "user@example.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        @Email
        String email,

        @Schema(
                description = "Секретная фраза пользователя",
                example = "Тортик",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        String secretPhrase
) {
}
