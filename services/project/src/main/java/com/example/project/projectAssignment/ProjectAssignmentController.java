package com.example.project.projectAssignment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/project-assignments")
@RequiredArgsConstructor
public class ProjectAssignmentController {
    private final ProjectAssignmentService service;

    @PostMapping("/assign")
    public ResponseEntity<Void> assignDeveloper(@RequestBody @Valid ProjectAssignmentRequest request) {
        service.assignDeveloper(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/unassign")
    public ResponseEntity<Void> unassignDeveloper(@RequestBody @Valid ProjectAssignmentRequest request) {
        service.unassignDeveloper(request);
        return ResponseEntity.accepted().build();
    }
}
