package com.example.auth.dto.auth;

public record AuthenticationResponse(
        String accessToken,
        String refreshToken
) {
}
