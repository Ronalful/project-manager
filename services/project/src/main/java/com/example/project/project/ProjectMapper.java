package com.example.project.project;

import com.example.project.developer.DeveloperClient;
import com.example.project.developer.DeveloperResponse;
import com.example.project.projectAssignment.ProjectAssignment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectMapper {

    private final DeveloperClient developerClient;

    public Project toProject(ProjectRequest request) {
        return Project.builder()
                .name(request.name())
                .description(request.description())
                .build();
    }

    public ProjectResponse fromProjectWithDevelopers(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                findAllDevelopersInProject(project)
        );
    }

    private List<DeveloperResponse> findAllDevelopersInProject(Project project) {
        if (project.getAssignments() == null) {
            return new ArrayList<>();
        }

        return project.getAssignments().stream()
                .map(this::findDeveloperInAssignment)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private Optional<DeveloperResponse> findDeveloperInAssignment(ProjectAssignment projectAssignment) {
        try {
            return developerClient.getDeveloperById(projectAssignment.getDeveloperId());
        }
        catch (Exception e) {
            return Optional.empty();
        }
    }
}
