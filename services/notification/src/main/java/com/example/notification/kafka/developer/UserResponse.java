package com.example.notification.kafka.developer;

public record UserResponse(
        Integer id,
        String firstname,
        String lastname,
        String email
) {
}
