package com.example.auth.controller;

import com.example.auth.dto.CreateUserRequest;
import com.example.auth.service.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/admin")
@RequiredArgsConstructor
@Tag(
        name = "Админ контроль над пользователями",
        description = "Контроллер для управления пользователями админом"
)
public class AdminUserController {
    private final AdminUserService service;

    @Operation(
            summary = "Создание пользователя с временным паролем",
            description = "Необходим доступ админа",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        service.createUser(request);
        return ResponseEntity.accepted().build();
    }

    @Operation(
            summary = "Создание админа с временным паролем",
            description = "Необходим доступ админа",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(@RequestBody CreateUserRequest request) {
        service.createAdmin(request);
        return ResponseEntity.accepted().build();
    }
}
