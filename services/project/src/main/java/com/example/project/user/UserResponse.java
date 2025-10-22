package com.example.project.user;

public record UserResponse(
        Integer id,
        String firstname,
        String lastname,
        String email,
        Role role
) {
}
