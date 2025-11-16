package com.example.project.project;

import com.example.project.user.UserClient;
import com.example.project.user.UserResponse;
import com.example.project.projectAssignment.ProjectAssignment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectMapper {

    private final UserClient userClient;

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

    private List<UserResponse> findAllDevelopersInProject(Project project) {
        if (project.getAssignments() == null) {
            return new ArrayList<>();
        }

        return project.getAssignments().stream()
                .map(this::findDeveloperInAssignment)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private Optional<UserResponse> findDeveloperInAssignment(ProjectAssignment projectAssignment) {
        try {
            return userClient.getUserById(projectAssignment.getUserId());
        }
        catch (Exception e) {
            return Optional.empty();
        }
    }
}
