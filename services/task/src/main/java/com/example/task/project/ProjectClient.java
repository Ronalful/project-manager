package com.example.task.project;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(
        name = "project-service",
        url = "${application.config.project-url}"
)
public interface ProjectClient {
    @GetMapping("/{project-id}")
    Optional<ProjectResponse> getProjectById(@PathVariable("project-id") Integer projectId);
    @GetMapping("/{project-id}")
    Optional<ProjectWithDevelopersResponse> getProjectByIdWithDevelopers(@PathVariable("project-id") Integer projectId);
}
