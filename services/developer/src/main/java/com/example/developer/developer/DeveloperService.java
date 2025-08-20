package com.example.developer.developer;

import com.example.developer.exception.DeveloperNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeveloperService {
    private final DeveloperRepository repository;
    private final DeveloperMapper mapper;

    @Transactional
    public DeveloperResponse create(DeveloperRequest request) {
        var developer = repository.save(mapper.toDeveloper(request));
        return mapper.fromDeveloper(developer);
    }

    public List<DeveloperResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::fromDeveloper)
                .toList();
    }

    public DeveloperResponse findById(Integer developerId) {
        return repository.findById(developerId)
                .map(mapper::fromDeveloper)
                .orElseThrow(() -> new DeveloperNotFoundException("Developer is not found with id " + developerId));
    }

    @Transactional
    public void delete(Integer developerId) {
        repository.deleteById(developerId);
    }

    @Transactional
    public DeveloperResponse update(@Valid DeveloperUpdateRequest request) {
        var developer = repository.findById(request.id())
                .orElseThrow(() -> new DeveloperNotFoundException("Developer is not found with id " + request.id()));
        developer.update(request);
        var updatedDeveloper = repository.save(developer);
        return mapper.fromDeveloper(updatedDeveloper);
    }
}
