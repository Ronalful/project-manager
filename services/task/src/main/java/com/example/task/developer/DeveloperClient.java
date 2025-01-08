package com.example.task.developer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(
        name = "developer-service",
        url = "${application.config.developer-url}"
)
public interface DeveloperClient {
    @GetMapping("/{developer-id}")
    Optional<DeveloperResponse> getDeveloperById(@PathVariable("developer-id") Integer developerId);
}
