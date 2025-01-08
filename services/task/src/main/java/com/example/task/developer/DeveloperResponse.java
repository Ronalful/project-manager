package com.example.task.developer;

public record DeveloperResponse(
        Integer id,
        String firstname,
        String lastname,
        String email
) {
}