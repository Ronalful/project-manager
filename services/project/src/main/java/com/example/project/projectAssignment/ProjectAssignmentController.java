package com.example.project.projectAssignment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/project-assignments")
@RequiredArgsConstructor
@Tag(
        name = "ProjectAssignmentController",
        description = "Данный контроллер отвечает за назначением разработчиков на проект"
)
public class ProjectAssignmentController {
    private final ProjectAssignmentService service;

    @PostMapping("/assign")
    @Operation(
            description = "Добавить разработчика на проект"
    )
    public ResponseEntity<Void> assignDeveloper(@RequestBody @Valid ProjectAssignmentRequest request) {
        service.assignDeveloper(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/unassign")
    @Operation(
            description = "Снять разработчика с проекта"
    )
    public ResponseEntity<Void> unassignDeveloper(@RequestBody @Valid ProjectAssignmentRequest request) {
        service.unassignDeveloper(request);
        return ResponseEntity.accepted().build();
    }
}
