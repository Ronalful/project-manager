package com.example.task.project;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(
        name = "admin-project-service",
        url = "${application.config.admin-project-url}"
)
public interface AdminProjectClient {
    @GetMapping("/{project-id}")
    Optional<ProjectResponse> getProjectById(@PathVariable("project-id") Integer projectId);
    @GetMapping("/{project-id}")
    Optional<ProjectWithDevelopersResponse> getProjectByIdWithDevelopers(@PathVariable("project-id") Integer projectId);
}
