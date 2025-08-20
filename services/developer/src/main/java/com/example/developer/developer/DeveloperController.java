package com.example.developer.developer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/developers")
@RequiredArgsConstructor
@Tag(
        name = "DeveloperController",
        description = "Данный контроллер отвечает за действия над разработчиками"
)
public class DeveloperController {

    private final DeveloperService service;

    @PostMapping
    @Operation(
            description = "Позволяет создать разработчика"
    )
    public ResponseEntity<DeveloperResponse> createDeveloper(@RequestBody @Valid DeveloperRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping
    @Operation(
            description = "Позволяет получить всех разработчиков"
    )
    public ResponseEntity<List<DeveloperResponse>> getDevelopers() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{developer-id}")
    @Operation(
            description = "Позволяет получить разработчика по id"
    )
    public ResponseEntity<DeveloperResponse> getDeveloperById(
            @PathVariable("developer-id")
            @Parameter(
                    description = "id",
                    example = "1",
                    required = true
            )  
            Integer developerId
    ) {
        return ResponseEntity.ok(service.findById(developerId));
    }

    @DeleteMapping("/{developer-id}")
    @Operation(
            description = "Позволяет удалить разработчика по id"
    )
    public ResponseEntity<Void> deleteDeveloper(
            @PathVariable("developer-id")
            @Parameter(
                    description = "id",
                    example = "1",
                    required = true
            )
            Integer developerId
    ) {
        service.delete(developerId);
        return ResponseEntity.accepted().build();
    }

    @PutMapping
    @Operation(
            description = "Позволяет обновить информацию о разработчике"
    )
    public ResponseEntity<DeveloperResponse> updateDeveloper(@RequestBody @Valid DeveloperUpdateRequest request) {
        return ResponseEntity.ok(service.update(request));
    }
}
