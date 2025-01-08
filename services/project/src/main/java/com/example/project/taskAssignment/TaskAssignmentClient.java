package com.example.project.taskAssignment;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "task-assignment-service",
        url = "${application.config.task-assignment-url}"
)
public interface TaskAssignmentClient {
    @PostMapping("/unassign")
    Void unassignDeveloper(@RequestBody @Valid TaskAssignmentRequest request);
}
