package com.example.developer.developer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/developers")
@RequiredArgsConstructor
public class DeveloperController {

    private final DeveloperService service;

    @PostMapping
    public ResponseEntity<DeveloperResponse> createDeveloper(@RequestBody @Valid DeveloperRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping
    public ResponseEntity<List<DeveloperResponse>> getDevelopers() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{developer-id}")
    public ResponseEntity<DeveloperResponse> getDeveloperById(@PathVariable("developer-id") Integer developerId) {
        return ResponseEntity.ok(service.findById(developerId));
    }

    @DeleteMapping("/{developer-id}")
    public ResponseEntity<Void> deleteDeveloper(@PathVariable("developer-id") Integer developerId) {
        service.delete(developerId);
        return ResponseEntity.accepted().build();
    }

    @PutMapping
    public ResponseEntity<DeveloperResponse> updateDeveloper(@RequestBody @Valid DeveloperUpdateRequest request) {
        return ResponseEntity.ok(service.update(request));
    }
}
