package com.example.task.comment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/comments")
@RequiredArgsConstructor
@Tag(
        name = "Управление комментариями к задачам"
)
public class UserCommentController {
    private final UserCommentService service;

    @PostMapping
    @Operation(
            description = "Создания комментария",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<CommentResponse> addComment(
            @RequestBody @Valid
            CommentRequest request,
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id")
            Integer userId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.add(request, userId));
    }

    @PutMapping
    @Operation(
            description = "Обновление комментария",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<CommentResponse> addComment(
            @RequestBody @Valid
            UpdateCommentRequest request,
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id")
            Long userId
    ) {
        return ResponseEntity.ok(service.update(request, userId));
    }

    @DeleteMapping("/{id}")
    @Operation(
            description = "Удаление комментария",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<?> deleteComment(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id")
            Long userId,
            @PathVariable
            Long id
    ) {
        service.delete(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(
            description = "Получение комментария по id",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<CommentResponse> getById(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id")
            Long userId,
            @PathVariable
            Long id
    ) {
        return ResponseEntity.ok(service.getById(userId, id));
    }

    @GetMapping("/task/{taskId}")
    @Operation(
            description = "Получение комментария по id",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<List<CommentResponse>> getByTaskId(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id")
            Long userId,
            @PathVariable
            Integer taskId
    ) {
        return ResponseEntity.ok(service.getByTaskId(userId, taskId));
    }
}
