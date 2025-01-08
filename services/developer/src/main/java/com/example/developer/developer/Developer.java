package com.example.developer.developer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Developer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;

    public void update(@Valid DeveloperUpdateRequest request) {
        if (request.firstname() != null) firstname = request.firstname();
        if (request.lastname() != null) lastname = request.lastname();
        if (request.email() != null) email = request.email();
    }
}
