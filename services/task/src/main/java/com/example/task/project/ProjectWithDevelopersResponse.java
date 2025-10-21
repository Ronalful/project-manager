package com.example.task.project;

import com.example.task.developer.UserResponse;

import java.util.List;

public record ProjectWithDevelopersResponse(
        Integer id,
        String name,
        String description,
        List<UserResponse> developers
) {
    public ProjectResponse toProjectResponse() {
        return new ProjectResponse(
                id,
                name,
                description
        );
    }
}
