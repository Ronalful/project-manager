package com.example.task.task;

import com.example.task.developer.DeveloperClient;
import com.example.task.developer.DeveloperResponse;
import com.example.task.project.ProjectClient;
import com.example.task.project.ProjectResponse;
import com.example.task.taskAssignment.TaskAssignment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskMapper {

    private final DeveloperClient developerClient;
    private final ProjectClient projectClient;

    public Task toTask(TaskRequest request) {
        Task task = Task.builder()
                .projectId(request.projectId())
                .title(request.title())
                .description(request.description())
                .dueDate(request.dueDate())
                .build();
        if (request.priority() != null) {
            task.setPriority(request.priority());
        }
        if (request.status() != null) {
            task.setStatus(request.status());
        }
        return task;
    }

    public TaskResponse fromTaskWithDevelopersAndProject(Task task) {
        return new TaskResponse(
                task.getId(),
                findProjectInTask(task),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getStatus(),
                task.getDueDate(),
                findAllDevelopersInTask(task),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );

    }

    private List<DeveloperResponse> findAllDevelopersInTask(Task task) {
        if (task.getAssignments() == null) {
            return new ArrayList<>();
        }

        return task.getAssignments().stream()
                .map(this::findDeveloperInAssignment)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private Optional<DeveloperResponse> findDeveloperInAssignment(TaskAssignment projectAssignment) {
        try {
            return developerClient.getDeveloperById(projectAssignment.getDeveloperId());
        }
        catch (Exception e) {
            return Optional.empty();
        }
    }

    private ProjectResponse findProjectInTask(Task task) {
        return projectClient.getProjectById(task.getProjectId()).get();
    }
}
