package com.example.task.comment;

import java.time.LocalDateTime;

public record CommentResponse(
        Integer id,
        Long userId,
        String text,
        Long taskId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}