package com.example.project.task;

import com.example.project.user.UserResponse;

import java.util.List;

public record TaskWithDevelopersResponse(
        Integer id,
        String title,
        String description,
        List<UserResponse> developers
) {
}
