package com.example.gateway.config;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class RouteValidator {
    public static final List<String> openApiEndpoints = new ArrayList<>(List.of(
            "/api/v1/auth/register",
            "/api/v1/auth/login"
    ));

    static {
        openApiEndpoints.addAll(getSwaggerForService("developers"));
        openApiEndpoints.addAll(getSwaggerForService("tasks"));
        openApiEndpoints.addAll(getSwaggerForService("projects"));
        openApiEndpoints.addAll(getSwaggerForService("auth"));
    }

    private static List<String> getSwaggerForService(String path) {
        var swaggerEndpoints = List.of(
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/webjars/**",
                "/swagger-ui/index.html",
                "/swagger-ui/swagger-initializer.js",
                "/swagger-ui/swagger-ui.css",
                "/swagger-ui/swagger-ui-bundle.js",
                "/swagger-ui/swagger-ui-standalone-preset.js"
        );

        return swaggerEndpoints.stream()
                .map(endpoint -> "/api/v1/" + path + endpoint)
                .toList();
    }

    private static final Map<String, List<String>> roleBasedEndpoints = Map.of(
            "ADMIN", List.of(
                    "/api/v1/developers/**",
                    "/api/v1/projects/**",
                    "/api/v1/tasks/**",
                    "/api/v1/auth/**"
            ),
            "USER", List.of(
                    "/api/v1/developers/**",
                    "/api/v1/projects/**",
                    "/api/v1/tasks/**",
                    "/api/v1/auth/**"
            )
    );

    public boolean isSecured(String path) {
        return openApiEndpoints
                .stream()
                .noneMatch(allowedPath -> {
                    if (allowedPath.endsWith("/**")) {
                        return path.startsWith(allowedPath.substring(0, allowedPath.length() - 3));
                    }

                    return path.equals(allowedPath);
                });
    }


    public boolean isEndpointAllowedForRole(String path, String role) {
        List<String> allowedPaths = roleBasedEndpoints.get(role);
        if (allowedPaths == null) return false;

        return allowedPaths.stream().anyMatch(allowedPath -> {
            if (allowedPath.endsWith("/**")) {
                 return path.startsWith(allowedPath.substring(0, allowedPath.length() - 3));
            }

            return path.equals(allowedPath);
        });
    }

    public boolean isRefreshTokenEndpoint(String path) {
        return path.equals("/api/v1/auth/refresh-token");
    }
}
