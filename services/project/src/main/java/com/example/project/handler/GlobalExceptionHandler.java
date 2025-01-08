package com.example.project.handler;

import com.example.project.exception.ProjectAssignmentExistsException;
import com.example.project.exception.ProjectAssignmentNotExistsException;
import com.example.project.exception.ProjectNotFoundException;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<String> handle(ProjectNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> handle(FeignException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.contentUTF8());
    }

    @ExceptionHandler(ProjectAssignmentExistsException.class)
    public ResponseEntity<String> handle(ProjectAssignmentExistsException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler(ProjectAssignmentNotExistsException.class)
    public ResponseEntity<String> handle(ProjectAssignmentNotExistsException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException e) {
        var errors = new HashMap<String, String>();
        e.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var fieldError = ((FieldError) error).getField();
                    var errorMessage = error.getDefaultMessage();
                    errors.put(fieldError, errorMessage);
                });
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(errors));
    }
}
