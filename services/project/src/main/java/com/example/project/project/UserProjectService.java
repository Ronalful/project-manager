package com.example.project.project;
import com.example.project.exception.ProjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper mapper;

    public List<ProjectResponse> findAll(Integer userId) {
        return projectRepository.findByAssignments_UserId(userId).stream()
                .map(mapper::fromProjectWithDevelopers)
                .toList();
    }

    public ProjectResponse findById(Integer projectId, Integer userId) {
        var project = projectRepository.findByAssignments_UserIdAndId(userId, projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project with id " + projectId + " not found"));
        return mapper.fromProjectWithDevelopers(project);
    }
}
