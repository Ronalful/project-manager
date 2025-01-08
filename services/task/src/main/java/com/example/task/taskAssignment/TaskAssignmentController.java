package com.example.task.taskAssignment;

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
public class TaskAssignmentController {

    private final TaskAssignmentService service;

    @PostMapping("/assign")
    public ResponseEntity<Void> assignDeveloper(@RequestBody @Valid TaskAssignmentRequest request) {
        service.assignDeveloper(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/unassign")
    public ResponseEntity<Void> unassignDeveloper(@RequestBody @Valid TaskAssignmentRequest request) {
        service.unassignDeveloper(request);
        return ResponseEntity.accepted().build();
    }
}
