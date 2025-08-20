package com.example.project.project;

import com.example.project.exception.ProjectNotFoundException;
import com.example.project.projectAssignment.ProjectAssignmentRequest;
import com.example.project.projectAssignment.ProjectAssignmentService;
import com.example.project.task.TaskClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectAssignmentService projectAssignmentService;
    private final TaskClient taskClient;
    private final ProjectMapper mapper;

    @Transactional
    public ProjectResponse create(ProjectRequest request) {
        var project = projectRepository.save(mapper.toProject(request));
        return mapper.fromProjectWithDevelopers(project);
    }

    public List<ProjectResponse> findAll() {
        return projectRepository.findAll().stream()
                .map(mapper::fromProjectWithDevelopers)
                .toList();
    }

    public ProjectResponse findById(Integer projectId) {
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id " + projectId));
        return mapper.fromProjectWithDevelopers(project);
    }

    @Transactional
    public void deleteById(Integer projectId) {
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id " + projectId));
        deleteTasksInProject(projectId);
        unassignDevelopersInProject(project);
        projectRepository.deleteById(projectId);
    }

    private void unassignDevelopersInProject(Project project) {
        for (var assignment : project.getAssignments()) {
            projectAssignmentService.unassignDeveloper(new ProjectAssignmentRequest(project.getId(), assignment.getDeveloperId()));
        }
    }

    private void deleteTasksInProject(Integer projectId) {
        var tasks = taskClient.getTasksByProjectId(projectId);
        tasks.forEach(task -> taskClient.deleteTaskById(task.id()));
    }

    @Transactional
    public ProjectResponse update(ProjectUpdateRequest request) {
        var project = projectRepository.findById(request.id())
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id " + request.id()));
        project.update(request);
        var updatedProject = projectRepository.save(project);
        return mapper.fromProjectWithDevelopers(updatedProject);
    }
}
