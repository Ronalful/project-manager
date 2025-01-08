package com.example.project.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
@Data
public class ProjectAssignmentNotExistsException extends RuntimeException {
    private final String message;
}
