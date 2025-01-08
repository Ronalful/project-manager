package com.example.project.project;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService service;

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody @Valid ProjectRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{project-id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable("project-id") Integer projectId) {
        return ResponseEntity.ok(service.findById(projectId));
    }

    @DeleteMapping("/{project-id}")
    public ResponseEntity<Void> deleteProject(@PathVariable("project-id") Integer projectId) {
        service.deleteById(projectId);
        return ResponseEntity.accepted().build();
    }

    @PutMapping
    public ResponseEntity<ProjectResponse> updateProject(@RequestBody @Valid ProjectUpdateRequest request) {
        return ResponseEntity.ok(service.update(request));
    }
}
