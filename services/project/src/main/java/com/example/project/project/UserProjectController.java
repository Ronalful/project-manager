package com.example.project.project;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/projects")
@RequiredArgsConstructor
@Tag(
        name = "Пользовательский контроль над проектами",
        description = "Данный контроллер отвечает за действия над проектами"
)
public class UserProjectController {
    private final UserProjectService service;

    @GetMapping
    @Operation(
            description = "Позволяет получить все проекты доступные разработчику",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<List<ProjectResponse>> getAllProjects(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") String userId
    ) {
        return ResponseEntity.ok(service.findAll(Integer.valueOf(userId)));
    }

    @GetMapping("/{project-id}")
    @Operation(
            description = "Позволяет получить проект по id доступный разработчику",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ProjectResponse> getProjectById(
            @PathVariable("project-id")
            @Parameter(
                    description = "id",
                    example = "1",
                    required = true
            )
            Integer projectId,
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") String userId
    ) {
        return ResponseEntity.ok(service.findById(projectId, Integer.valueOf(userId)));
    }
}
