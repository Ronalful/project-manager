package com.example.task.project;

import com.example.task.developer.DeveloperResponse;

import java.util.List;

public record ProjectWithDevelopersResponse(
        Integer id,
        String name,
        String description,
        List<DeveloperResponse> developers
) {
    public ProjectResponse toProjectResponse() {
        return new ProjectResponse(
                id,
                name,
                description
        );
    }
}
