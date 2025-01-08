package com.example.task.task;

import com.example.task.developer.DeveloperClient;
import com.example.task.exception.DeveloperNotFoundException;
import com.example.task.exception.ProjectNotFoundException;
import com.example.task.exception.TaskNotFoundException;
import com.example.task.kafka.TaskChangedStateNotification;
import com.example.task.kafka.TaskProducer;
import com.example.task.project.ProjectClient;
import com.example.task.taskAssignment.TaskAssignmentRequest;
import com.example.task.taskAssignment.TaskAssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper mapper;
    private final ProjectClient projectClient;
    private final TaskProducer taskProducer;
    private final DeveloperClient developerClient;
    private final TaskAssignmentService taskAssignmentService;

    public TaskResponse create(TaskRequest request) {
        if (!isProjectExistsWithId(request.projectId())) {
            throw new ProjectNotFoundException("Project not found with id " + request.projectId());
        }
        var task = taskRepository.save(mapper.toTask(request));
        return mapper.fromTaskWithDevelopersAndProject(task);
    }

    private boolean isProjectExistsWithId(Integer projectId) {
        return projectClient.getProjectById(projectId).isPresent();
    }

    public List<TaskResponse> findAll() {
        return taskRepository.findAll().stream()
                .map(mapper::fromTaskWithDevelopersAndProject)
                .toList();
    }

    public TaskResponse findById(Integer taskId) {
        return taskRepository.findById(taskId)
                .map(mapper::fromTaskWithDevelopersAndProject)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id " + taskId));
    }

    public void deleteById(Integer taskId) {
        var task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id " + taskId));
        unassignDevelopersInTask(task);
        taskRepository.deleteById(taskId);
    }

    private void unassignDevelopersInTask(Task task) {
        for (var assignment : task.getAssignments()) {
            taskAssignmentService.unassignDeveloper(new TaskAssignmentRequest(task.getId(), assignment.getDeveloperId()));
        }
    }

    public List<TaskResponse> findByProjectId(Integer projectId) {
        return taskRepository.findByProjectId(projectId).stream()
                .map(mapper::fromTaskWithDevelopersAndProject)
                .toList();
    }

    public TaskResponse update(TaskUpdateRequest request) {
        var task = taskRepository.findById(request.id())
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
        var project = projectClient.getProjectById(task.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id " + task.getProjectId()));
        for (var assignment : task.getAssignments()) {
            var developer = developerClient.getDeveloperById(assignment.getDeveloperId())
                    .orElseThrow(() -> new DeveloperNotFoundException("Developer " + assignment.getDeveloperId() + " not found"));

            taskProducer.send(new TaskChangedStateNotification(
                    developer,
                    task.getTitle(),
                    task.getStatus(),
                    project
            ));
        }
    }
}
