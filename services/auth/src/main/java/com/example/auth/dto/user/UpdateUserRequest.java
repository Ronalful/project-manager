package com.example.auth.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Запрос на обновление пользователя")
public record UpdateUserRequest(
        @Schema(
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull
        Long userId,

        @Schema(
                description = "Имя пользователя",
                example = "Иван",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        String firstname,

        @Schema(
                description = "Фамилия пользователя",
                example = "Иванов",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        String lastname,

        @Schema(
                description = "Пароль пользователя",
                example = "securePassword123",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        String password,

        @Schema(
                description = "Секретное слово",
                example = "Тортик",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        String secretPhrase,

        @Schema(
                description = "Активирована ли запись",
                example = "true",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        Boolean activated,

        @Schema(
                 description = "Истек ли пароль",
                 example = "false",
                 requiredMode = Schema.RequiredMode.NOT_REQUIRED
         )
        Boolean passwordExpired
) {
}
