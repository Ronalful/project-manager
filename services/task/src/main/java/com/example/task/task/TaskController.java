package com.example.task.task;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody @Valid TaskRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{task-id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable("task-id") Integer taskId) {
        return ResponseEntity.ok(service.findById(taskId));
    }

    @DeleteMapping("/{task-id}")
    public ResponseEntity<Void> deleteTaskById(@PathVariable("task-id") Integer taskId) {
        service.deleteById(taskId);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/in-project/{project-id}")
    public ResponseEntity<List<TaskResponse>> getTasksByProjectId(@PathVariable("project-id") Integer projectId) {
        return ResponseEntity.ok(service.findByProjectId(projectId));
    }

    @PutMapping
    public ResponseEntity<TaskResponse> updateTask(@RequestBody @Valid TaskUpdateRequest request) {
        return ResponseEntity.ok(service.update(request));
    }
}
