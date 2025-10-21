package com.example.task.task;

import com.example.task.developer.UserResponse;
import com.example.task.project.ProjectResponse;

import java.util.List;

public record TaskWithDevelopersAndProject(
        TaskRequest task,
        ProjectResponse project,
        List<UserResponse> developers
) {
}
