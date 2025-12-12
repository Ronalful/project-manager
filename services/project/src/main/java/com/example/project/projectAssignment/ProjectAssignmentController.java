package com.example.project.projectAssignment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/project-assignments")
@RequiredArgsConstructor
@Tag(
        name = "Назначения сотрудников на проект админом",
        description = "Данный контроллер отвечает за назначением разработчиков на проект"
)
public class ProjectAssignmentController {
    private final ProjectAssignmentService service;

    @PostMapping("/assign")
    @Operation(
            description = "Добавить разработчиков на проект",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ProjectAssignmentResponse> assignUsers(@RequestBody @Valid ProjectAssignmentRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.assignUsers(request));
    }

    @PostMapping("/unassign")
    @Operation(
            description = "Снять разработчика с проекта",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ProjectAssignmentResponse> unassignUsers(@RequestBody @Valid ProjectAssignmentRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.unassignUsers(request));
    }
}
