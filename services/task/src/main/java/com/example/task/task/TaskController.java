package com.example.task.task;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(
        name = "TaskController",
        description = "Данный контроллер отвечает за действия над задачами"
)
public class TaskController {

    private final TaskService service;

    @PostMapping
    @Operation(
            description = "Позволяет создать задачу"
    )
    public ResponseEntity<TaskResponse> createTask(@RequestBody @Valid TaskRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping
    @Operation(
            description = "Позволяет получить все задачи"
    )
    public ResponseEntity<List<TaskResponse>> getTasks() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{task-id}")
    @Operation(
            description = "Позволяет получить задачу по id"
    )
    public ResponseEntity<TaskResponse> getTaskById(
            @PathVariable("task-id")
            @Parameter(
                    description = "id",
                    example = "1",
                    required = true
            )
            Integer taskId
    ) {
        return ResponseEntity.ok(service.findById(taskId));
    }

    @DeleteMapping("/{task-id}")
    @Operation(
            description = "Позволяет удалить задачу по id"
    )
    public ResponseEntity<Void> deleteTaskById(
            @PathVariable("task-id")
            @Parameter(
                    description = "id",
                    example = "1",
                    required = true
            )
            Integer taskId
    ) {
        service.deleteById(taskId);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/in-project/{project-id}")
    @Operation(
            description = "Позволяет получить все задачи из проекта"
    )
    public ResponseEntity<List<TaskResponse>> getTasksByProjectId(
            @PathVariable("project-id")
            @Parameter(
                    description = "id",
                    example = "1",
                    required = true
            )
            Integer projectId) {
        return ResponseEntity.ok(service.findByProjectId(projectId));
    }

    @PutMapping
    @Operation(
            description = "Позволяет обновить информацию о задаче"
    )
    public ResponseEntity<TaskResponse> updateTask(@RequestBody @Valid TaskUpdateRequest request) {
        return ResponseEntity.ok(service.update(request));
    }
}
