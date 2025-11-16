package com.example.gateway.client;

import com.example.gateway.dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserClient {
    private final WebClient webClient;

    public UserClient(WebClient.Builder webClientBuilder,
                      @Value("${app.user-url}") String userUrl) {
        this.webClient = webClientBuilder.baseUrl(userUrl).build();
    }

    public Mono<UserDTO> getUserByEmail(String email) {
        return webClient.get()
                .uri("/email/{email}", email)
                .retrieve()
                .bodyToMono(UserDTO.class);
    }
}
