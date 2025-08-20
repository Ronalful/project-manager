package com.example.task.taskAssignment;

import com.example.task.developer.DeveloperClient;
import com.example.task.developer.DeveloperResponse;
import com.example.task.exception.*;
import com.example.task.kafka.TaskNotification;
import com.example.task.kafka.TaskOperation;
import com.example.task.kafka.TaskProducer;
import com.example.task.project.ProjectClient;
import com.example.task.project.ProjectResponse;
import com.example.task.project.ProjectWithDevelopersResponse;
import com.example.task.task.Task;
import com.example.task.task.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskAssignmentService {

    private final TaskRepository taskRepository;
    private final TaskAssignmentRepository taskAssignmentRepository;
    private final DeveloperClient developerClient;
    private final ProjectClient projectClient;
    private final TaskProducer taskProducer;

    @Transactional
    public void assignDeveloper(TaskAssignmentRequest request) {
        var developer = developerClient.getDeveloperById(request.developerId())
                .orElseThrow(() -> new DeveloperNotFoundException("Developer not found with id " + request.developerId()));
        var task = taskRepository.findById(request.taskId())
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id " + request.taskId()));
        var project = projectClient.getProjectByIdWithDevelopers(task.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id " + task.getProjectId()));

        if (isDeveloperInProject(request.developerId(), project)) {
            throw new DeveloperIsNotInProjectException("This developer is not in project with id " + task.getProjectId());
        }

        if (isDeveloperAssignedToTask(request.developerId(), task)) {
            throw new TaskAssignmentExistsException("This developer is already assigned to this task");
        }

        sendDeveloperAssignedToTaskInProjectNotification(developer, task, project.toProjectResponse());

        taskAssignmentRepository.save(TaskAssignment.builder()
                .task(task)
                .developerId(developer.id())
                .build());
    }

    private boolean isDeveloperInProject(Integer developerId, ProjectWithDevelopersResponse project) {
        return project.developers().stream()
                .filter(dev -> dev.id().equals(developerId))
                .toList()
                .isEmpty();
    }

    private boolean isDeveloperAssignedToTask(Integer developerId, Task task) {
        return taskAssignmentRepository.findByTaskAndDeveloperId(task, developerId)
                .isPresent();
    }

    private void sendDeveloperAssignedToTaskInProjectNotification(DeveloperResponse developer, Task task,
                                                                  ProjectResponse project) {
        taskProducer.send(new TaskNotification(
                developer,
                task.getTitle(),
                project,
                TaskOperation.ASSIGNED
        ));
    }

    private void sendDeveloperUnassignedToTaskInProjectNotification(DeveloperResponse developer, Task task,
                                                                  ProjectResponse project) {
        taskProducer.send(new TaskNotification(
                developer,
                task.getTitle(),
                project,
                TaskOperation.UNASSIGNED
        ));
    }
    @Transactional
    public void unassignDeveloper(TaskAssignmentRequest request) {
        var developer = developerClient.getDeveloperById(request.developerId())
                .orElseThrow(() -> new DeveloperNotFoundException("Developer not found with id " + request.developerId()));
        var task = taskRepository.findById(request.taskId())
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id " + request.taskId()));
        var project = projectClient.getProjectByIdWithDevelopers(task.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id " + task.getProjectId()));

        if(!isDeveloperAssignedToTask(request.developerId(), task)) {
            throw new TaskAssignmentNotExistsException("This developer is not assigned to this task");
        }

        sendDeveloperUnassignedToTaskInProjectNotification(developer, task, project.toProjectResponse());

        taskAssignmentRepository.deleteByTaskAndDeveloperId(task, request.developerId());
    }
}

