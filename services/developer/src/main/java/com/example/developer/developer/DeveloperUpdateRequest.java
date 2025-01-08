package com.example.developer.developer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record DeveloperUpdateRequest(
        @NotNull(message = "Developer id is required")
        Integer id,
        String firstname,
        String lastname,
        @Email(message = "Developer email is not valid")
        String email
) {
}
