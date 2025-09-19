package com.example.gateway.security;

import com.example.gateway.client.AuthClient;
import com.example.gateway.config.RouteValidator;
import com.example.gateway.service.JwtService;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    private final RouteValidator routeValidator;
    private final JwtService jwtService;
    private final AuthClient authClient;

    public JwtAuthenticationFilter(RouteValidator routeValidator, JwtService jwtService, AuthClient authClient) {
        super(Config.class);
        this.routeValidator = routeValidator;
        this.jwtService = jwtService;
        this.authClient = authClient;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            var request = exchange.getRequest();
            var path = request.getURI().getPath();

            if (!routeValidator.isSecured(path)) {
                return chain.filter(exchange);
            }

            if (!hasBearerToken(request)) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            String token = getJwtToken(request);
            String userEmail;

            try {
                userEmail = jwtService.extractUsername(token);
            } catch (Exception e) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            if (routeValidator.isRefreshTokenEndpoint(path)) {
                if (jwtService.isTokenValid(token, userEmail)) {
                    return chain.filter(exchange);
                }
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            return authClient.isTokenValid(token)
                    .flatMap(valid -> {
                        if (!valid) {
                            return onError(exchange, HttpStatus.UNAUTHORIZED);
                        }

                        return authClient.getUserByEmail(userEmail)
                                .flatMap(user -> {
                                    String role = user.role().getAuthority().replace("ROLE_", "");

                                    boolean authorized = routeValidator.isEndpointAllowedForRole(path, role);
                                    if (!authorized) {
                                        return onError(exchange, HttpStatus.FORBIDDEN);
                                    }

                                    return chain.filter(exchange);
                                });
                    });
        };
    }

    public static class Config {
    }

    private boolean hasBearerToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        return authHeader != null && authHeader.startsWith("Bearer ");
    }

    private String getJwtToken(ServerHttpRequest request) {
        return request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION).substring(7);
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }
}