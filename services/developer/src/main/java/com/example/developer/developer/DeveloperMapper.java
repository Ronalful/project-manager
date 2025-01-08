package com.example.developer.developer;

import org.springframework.stereotype.Service;

@Service
public class DeveloperMapper {

    public Developer toDeveloper(DeveloperRequest request) {
        return Developer.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .email(request.email())
                .build();
    }

    public DeveloperResponse fromDeveloper(Developer developer) {
        return new DeveloperResponse(
                developer.getId(),
                developer.getFirstname(),
                developer.getLastname(),
                developer.getEmail()
        );
    }
}
