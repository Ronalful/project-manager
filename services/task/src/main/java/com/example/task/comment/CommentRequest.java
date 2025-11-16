package com.example.task.comment;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentRequest(
        @NotBlank
        String text,
        @NotNull
        Integer taskId
){
}