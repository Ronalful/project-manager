package com.example.project.project;

import com.example.project.user.UserResponse;

import java.util.List;

public record ProjectResponse(
        Integer id,
        String name,
        String description,
        List<UserResponse> developers
) {
}
