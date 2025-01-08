package com.example.developer.developer;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record DeveloperRequest(
        @NotNull(message = "Developer firstname is required")
        String firstname,
        @NotNull(message = "Developer lastname is required")
        String lastname,
        @NotNull(message = "Developer email is required")
        @Email(message = "Developer email is not valid")
        String email
) {
}
