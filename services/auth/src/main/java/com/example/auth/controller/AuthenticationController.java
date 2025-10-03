package com.example.auth.controller;

import com.example.auth.dto.ActivationRequest;
import com.example.auth.dto.AuthenticationRequest;
import com.example.auth.dto.AuthenticationResponse;
import com.example.auth.entity.user.User;
import com.example.auth.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(
        name = "Аутентификация",
        description = "Контроллер для регистрации, входа, обновления токена и выхода из системы"
)
public class AuthenticationController {

    private final AuthenticationService service;

    @Operation(
            summary = "Вход пользователя",
            description = "Аутентификация пользователя по логину и паролю с выдачей JWT токена"
    )
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(service.login(request));
    }

    @Operation(
            summary = "Активация пользователя",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/activate")
    public ResponseEntity<?> activate(
            @RequestHeader("X-User-Email") String userEmail,
            @RequestBody @Valid ActivationRequest request
    ) {
        service.activate(userEmail, request);
        return ResponseEntity.accepted().build();
    }

    @Operation(
            summary = "Пре активация пользователя",
            description = "Проверка пользователя, что он ни разу не заходил до этого, и выдача ему временного jwt токена"
    )
    @PostMapping("/pre-activate")
    public ResponseEntity<AuthenticationResponse> preActivate(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(service.preActivate(request));
    }

    @Operation(
            summary = "Обновление токена",
            description = "Обновление access-токена по refresh-токену (необходим JWT)",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refresh(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
        return ResponseEntity.accepted().build();
    }

    @Operation(
            summary = "Выход из системы",
            description = "Выход пользователя из системы. Должен быть авторизован",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        service.logout(request, response);
        return ResponseEntity.accepted().build();
    }

    @Operation(
            summary = "Получить пользователя по email"
    )
    @GetMapping("/user/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(service.getUserByEmail(email));
    }

    @Operation(
            summary = "Проверка валидности jwt токена"
    )
    @GetMapping("/is-token-valid/{token}")
    public ResponseEntity<Boolean> isTokenValid(@PathVariable String token) {
        return ResponseEntity.ok(service.isTokenValid(token));
    }
}
