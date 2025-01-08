package com.example.project.project;

import com.example.project.developer.DeveloperResponse;

import java.util.List;

public record ProjectResponse(
        Integer id,
        String name,
        String description,
        List<DeveloperResponse> developers
) {
}
