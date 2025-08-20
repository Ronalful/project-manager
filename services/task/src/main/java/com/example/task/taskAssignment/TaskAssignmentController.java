package com.example.task.taskAssignment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/task-assignments")
@RequiredArgsConstructor
@Tag(
        name = "TaskAssignmentController",
        description = "Данный контроллер отвечает за назначением разработчиков на задачу"
)
public class TaskAssignmentController {

    private final TaskAssignmentService service;

    @PostMapping("/assign")
    @Operation(
            description = "Добавить разработчика к задаче"
    )
    public ResponseEntity<Void> assignDeveloper(@RequestBody @Valid TaskAssignmentRequest request) {
        service.assignDeveloper(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/unassign")
    @Operation(
            description = "Снять разработчика с задачи"
    )
    public ResponseEntity<Void> unassignDeveloper(@RequestBody @Valid TaskAssignmentRequest request) {
        service.unassignDeveloper(request);
        return ResponseEntity.accepted().build();
    }
}
