package com.example.task.task;

import com.example.task.developer.UserClient;
import com.example.task.developer.UserResponse;
import com.example.task.project.AdminProjectClient;
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

    private final UserClient userClient;
    private final AdminProjectClient adminProjectClient;

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

    private List<UserResponse> findAllDevelopersInTask(Task task) {
        if (task.getAssignments() == null) {
            return new ArrayList<>();
        }

        return task.getAssignments().stream()
                .map(this::findDeveloperInAssignment)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private Optional<UserResponse> findDeveloperInAssignment(TaskAssignment projectAssignment) {
        try {
            return userClient.getUserById(projectAssignment.getUserId());
        }
        catch (Exception e) {
            return Optional.empty();
        }
    }

    private ProjectResponse findProjectInTask(Task task) {
        return adminProjectClient.getProjectById(task.getProjectId()).get();
    }
}
