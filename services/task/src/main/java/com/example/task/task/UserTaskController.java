package com.example.task.task;

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
@RequestMapping("/api/v1/user/tasks")
@RequiredArgsConstructor
@Tag(
        name = "Управление задачами пользователем",
        description = "Данный контроллер отвечает за действия над задачами"
)
public class UserTaskController {
    private final UserTaskService service;

    @GetMapping
    @Operation(
            description = "Позволяет получить все задачи",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<List<TaskResponse>> getTasks(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") String userId
    ) {
        return ResponseEntity.ok(service.findAll(Integer.valueOf(userId)));
    }

    @GetMapping("/{task-id}")
    @Operation(
            description = "Позволяет получить задачу по id",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<TaskResponse> getTaskById(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") String userId,

            @PathVariable("task-id")
            @Parameter(
                    description = "id",
                    example = "1",
                    required = true
            )
            Integer taskId
    ) {
        return ResponseEntity.ok(service.findById(Integer.valueOf(userId), taskId));
    }

    @PutMapping
    @Operation(
            description = "Позволяет обновить информацию о задаче",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<TaskResponse> updateTask(
            @RequestBody
            @Valid TaskUpdateRequest request,

            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") String userId
    ) {
        return ResponseEntity.ok(service.update(request, Integer.valueOf(userId)));
    }

    @GetMapping("/in-project/{project-id}")
    @Operation(
            description = "Позволяет получить все задачи из проекта",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<List<TaskResponse>> getTasksByProjectId(
            @PathVariable("project-id")
            @Parameter(
                    description = "id",
                    example = "1",
                    required = true
            )
            Integer projectId,
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") String userId
    )
    {
        return ResponseEntity.ok(service.findByProjectId(projectId, Integer.valueOf(userId)));
    }
}
