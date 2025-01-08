package com.example.project.task;

import com.example.project.developer.DeveloperResponse;

import java.util.List;

public record TaskWithDevelopersResponse(
        Integer id,
        String title,
        String description,
        List<DeveloperResponse> developers
) {
}
