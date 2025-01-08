package com.example.project.task;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "task-service",
        url = "${application.config.task-url}"
)
public interface TaskClient {
    @GetMapping("/in-project/{project-id}")
    List<TaskResponse> getTasksByProjectId(@PathVariable("project-id") Integer projectId);

    @GetMapping("/in-project/{project-id}")
    List<TaskWithDevelopersResponse> getTasksByProjectIdWithDevelopers(@PathVariable("project-id") Integer projectId);

    @DeleteMapping("/{task-id}")
    Void deleteTaskById(@PathVariable("task-id") Integer taskId);


}
