package com.example.task.project;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(
        name = "user-project-service",
        url = "${application.config.user-project-url}"
)
public interface UserProjectClient {
    @GetMapping
    List<ProjectResponse> getProjects(@RequestHeader("X-User-Id") String userId);
}
