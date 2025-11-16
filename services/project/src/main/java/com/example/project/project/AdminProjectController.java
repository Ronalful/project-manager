package com.example.project.project;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/projects")
@RequiredArgsConstructor
@Tag(
        name = "Админ контроль над проектами",
        description = "Данный контроллер отвечает за действия над проектами"
)
public class AdminProjectController {

    private final AdminProjectService service;

    @PostMapping
    @Operation(
            description = "Позволяет создать проект",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ProjectResponse> createProject(@RequestBody @Valid ProjectRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping
    @Operation(
            description = "Позволяет получить все проекты",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{project-id}")
    @Operation(
            description = "Позволяет получить проект по id",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ProjectResponse> getProjectById(
            @PathVariable("project-id")
            @Parameter(
                    description = "id",
                    example = "1",
                    required = true
            )
            Integer projectId
    ) {
        return ResponseEntity.ok(service.findById(projectId));
    }

    @DeleteMapping("/{project-id}")
    @Operation(
            description = "Позволяет удалить проект по id",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Void> deleteProject(
            @PathVariable("project-id")
            @Parameter(
                    description = "id",
                    example = "1",
                    required = true
            )
            Integer projectId) {
        service.deleteById(projectId);
        return ResponseEntity.accepted().build();
    }

    @PutMapping
    @Operation(
            description = "Позволяет обновить информацию о проекте",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ProjectResponse> updateProject(@RequestBody @Valid ProjectUpdateRequest request) {
        return ResponseEntity.ok(service.update(request));
    }
}
