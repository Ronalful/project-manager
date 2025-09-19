package com.example.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запрос на регистрацию нового пользователя")
public record RegisterRequest(

        @Schema(
                description = "Имя пользователя",
                example = "Иван",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        String firstname,

        @Schema(
                description = "Фамилия пользователя",
                example = "Иванов",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        String lastname,

        @Schema(
                description = "Email пользователя",
                example = "ivanov@example.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Email
        String email,

        @Schema(
                description = "Пароль пользователя",
                example = "securePassword123",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        String password

) {
}