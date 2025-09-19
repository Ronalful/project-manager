package com.example.gateway.client;

import com.example.gateway.dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthClient {
    private final WebClient webClient;

    public AuthClient(WebClient.Builder webClientBuilder,
                               @Value("${app.auth-url}") String authUrl) {
        this.webClient = webClientBuilder.baseUrl(authUrl).build();
    }

    public Mono<Boolean> isTokenValid(String token) {
        return webClient.get()
                .uri("/is-token-valid/{token}", token)
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    public Mono<UserDTO> getUserByEmail(String email) {
        return webClient.get()
                .uri("/user/{email}", email)
                .retrieve()
                .bodyToMono(UserDTO.class);
    }
}
