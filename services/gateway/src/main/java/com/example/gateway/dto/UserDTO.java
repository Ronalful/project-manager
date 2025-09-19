package com.example.gateway.dto;

public record UserDTO(
        Long id,
        String firstname,
        String lastname,
        String email,
        String password,
        Role role
) {
}
