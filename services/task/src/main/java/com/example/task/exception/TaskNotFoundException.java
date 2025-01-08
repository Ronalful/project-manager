package com.example.task.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class TaskNotFoundException extends RuntimeException {
    private final String message;
}
