package com.example.auth.controller;

import com.example.auth.dto.user.AdminUserResponse;
import com.example.auth.dto.user.CreateUserRequest;
import com.example.auth.dto.user.UpdateUserRequest;
import com.example.auth.service.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/users")
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

    @PatchMapping
    @Operation(
            description = "Позволяет обновить информацию о разработчике. Обновляются только указанные поля",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest request) {
        service.update(request);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{user-id}")
    @Operation(
            description = "Позволяет удалить пользователя по id",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Void> deleteUser(
            @PathVariable("user-id")
            @Parameter(
                    description = "id",
                    example = "1",
                    required = true
            )
            Long userId
    ) {
        service.delete(userId);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    @Operation(
            description = "Позволяет получить всех разработчиков",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<List<AdminUserResponse>> getUsers() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{user-id}")
    @Operation(
            description = "Позволяет получить пользователя по id",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<AdminUserResponse> getUserById(
            @PathVariable("user-id")
            @Parameter(
                    description = "id",
                    example = "1",
                    required = true
            )
            Long userId
    ) {
        return ResponseEntity.ok(service.findById(userId));
    }

    @Operation(
            summary = "Получить пользователя по email"
    )
    @GetMapping("/email/{email}")
    public ResponseEntity<AdminUserResponse> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(service.getUserByEmail(email));
    }
}
