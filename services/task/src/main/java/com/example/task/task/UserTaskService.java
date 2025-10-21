package com.example.task.task;

import com.example.task.project.ProjectResponse;
import com.example.task.project.UserProjectClient;
import com.example.task.developer.UserClient;
import com.example.task.exception.DeveloperIsNotInProjectException;
import com.example.task.exception.DeveloperNotFoundException;
import com.example.task.exception.ProjectNotFoundException;
import com.example.task.exception.TaskNotFoundException;
import com.example.task.kafka.TaskChangedStateNotification;
import com.example.task.kafka.TaskProducer;
import com.example.task.project.AdminProjectClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserTaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper mapper;

    private final AdminProjectClient adminProjectClient;
    private final UserProjectClient userProjectClient;
    private final UserClient userClient;
    private final TaskProducer taskProducer;


    public List<TaskResponse> findAll(Integer userId) {
        var projectIds = userProjectClient.getProjects(userId.toString()).stream()
                .map(ProjectResponse::id)
                .toList();
        return taskRepository.findByProjectIdIn(projectIds).stream()
                .map(mapper::fromTaskWithDevelopersAndProject)
                .toList();
    }
    public TaskResponse findById(Integer userId, Integer taskId) {
        var projectIds = userProjectClient.getProjects(userId.toString()).stream()
                .map(ProjectResponse::id)
                .toList();
        var task = taskRepository.findByIdAndProjectIdIn(taskId, projectIds)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        return mapper.fromTaskWithDevelopersAndProject(task);
    }

    @Transactional
    public TaskResponse update(TaskUpdateRequest request, Integer userId) {
        var task = taskRepository.findByIdAndAssignments_UserId(request.id(), userId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id " + request.id()));
        var taskStatus = task.getStatus();
        task.update(request);
        var updatedTask = taskRepository.save(task);

        if (request.status() != null && request.status() != taskStatus) {
            sendDeveloperTaskChangedStatusInProjectNotification(updatedTask);
        }

        return mapper.fromTaskWithDevelopersAndProject(updatedTask);
    }

    private void sendDeveloperTaskChangedStatusInProjectNotification(Task task) {
        var project = adminProjectClient.getProjectById(task.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id " + task.getProjectId()));
        for (var assignment : task.getAssignments()) {
            var developer = userClient.getUserById(assignment.getUserId())
                    .orElseThrow(() -> new DeveloperNotFoundException("Developer " + assignment.getUserId() + " not found"));

            taskProducer.send(new TaskChangedStateNotification(
                    developer,
                    task.getTitle(),
                    task.getStatus(),
                    project
            ));
        }
    }

    public List<TaskResponse> findByProjectId(Integer projectId, Integer userId) {
        var project = adminProjectClient.getProjectByIdWithDevelopers(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id " + projectId));
        var isDeveloperInProject = project.developers().stream()
                .anyMatch(developer -> developer.id().equals(userId));
        if (!isDeveloperInProject) {
            throw new DeveloperIsNotInProjectException("Developer " + userId + " is not in project " + projectId);
        }
        return taskRepository.findById(projectId).stream()
                .map(mapper::fromTaskWithDevelopersAndProject)
                .toList();
    }
}
