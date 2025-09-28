package com.example.auth.controller;

import com.example.auth.dto.AuthenticationRequest;
import com.example.auth.dto.AuthenticationResponse;
import com.example.auth.dto.RegisterRequest;
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
            summary = "Регистрация нового пользователя",
            description = "Создание нового пользователя и выдача токена доступа"
    )
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @Operation(
            summary = "Вход пользователя",
            description = "Аутентификация пользователя по логину и паролю с выдачей JWT токена"
    )
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(service.login(request));
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

    @GetMapping("/user/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(service.getUserByEmail(email));
    }

    @GetMapping("/is-token-valid/{token}")
    public ResponseEntity<Boolean> isTokenValid(@PathVariable String token) {
        return ResponseEntity.ok(service.isTokenValid(token));
    }
}
