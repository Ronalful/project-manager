package com.example.auth.controller;

import com.example.auth.dto.user.UserResponse;
import com.example.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/all/users")
@RequiredArgsConstructor
@Tag(
        name = "Контроллер пользователей (для всех ролей)"
)
public class UserController {
    private final UserService service;

    @GetMapping("/info")
    @Operation(
            description = "Позволяет получить информацию о пользователе",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<UserResponse> getAllProjects(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") Long userId
    ) {
        return ResponseEntity.ok(service.getInfo(userId));
    }
}
