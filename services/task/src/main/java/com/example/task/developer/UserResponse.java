package com.example.task.developer;

public record UserResponse(
        Integer id,
        String firstname,
        String lastname,
        String email
) {
}