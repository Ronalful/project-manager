package com.example.project.projectAssignment;

import com.example.project.user.UserClient;
import com.example.project.user.UserResponse;
import com.example.project.user.Role;
import com.example.project.exception.*;
import com.example.project.kafka.ProjectNotification;
import com.example.project.kafka.ProjectOperation;
import com.example.project.kafka.ProjectProducer;
import com.example.project.project.Project;
import com.example.project.project.ProjectRepository;
import com.example.project.task.TaskClient;
import com.example.project.taskAssignment.TaskAssignmentClient;
import com.example.project.taskAssignment.TaskAssignmentRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectAssignmentService {

    private final ProjectAssignmentRepository projectAssignmentRepository;
    private final ProjectRepository projectRepository;
    private final UserClient userClient;
    private final ProjectProducer projectProducer;
    private final TaskClient taskClient;
    private final TaskAssignmentClient taskAssignmentClient;

    @Transactional
    public ProjectAssignmentResponse assignUsers(ProjectAssignmentRequest request) {
        var project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id " + request.projectId()));

        var users = request.userIds().stream()
                .map(userClient::getUserById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(it -> it.role().equals(Role.USER))
                .filter(it -> !isDeveloperAssignedToProject(it.id(), project))
                .toList();

        users.forEach(user -> {
            sendDeveloperAssignedToProjectNotification(user, project);
            projectAssignmentRepository.save(ProjectAssignment.builder()
                    .project(project)
                    .userId(user.id())
                    .build());
        });

        return new ProjectAssignmentResponse(
                request.projectId(),
                users.stream()
                        .map(UserResponse::id)
                        .toList()
        );
    }

    private boolean isDeveloperAssignedToProject(Integer developerId, Project project) {
        return projectAssignmentRepository
                .findByProjectAndUserId(project, developerId)
                .isPresent();
    }

    private void sendDeveloperAssignedToProjectNotification(UserResponse user, Project project) {
        projectProducer.send(new ProjectNotification(
                user,
                project.getName(),
                ProjectOperation.ASSIGNED
        ));
    }

    private void sendDeveloperUnassignedToProjectNotification(UserResponse developer, Project project) {
        projectProducer.send(new ProjectNotification(
                developer,
                project.getName(),
                ProjectOperation.UNASSIGNED
        ));
    }

    @Transactional
    public ProjectAssignmentResponse unassignUsers(ProjectAssignmentRequest request) {
        var project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id " + request.projectId()));

        var users = request.userIds().stream()
                .map(userClient::getUserById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(it -> it.role().equals(Role.USER))
                .filter(it -> isDeveloperAssignedToProject(it.id(), project))
                .toList();


        users.forEach(user -> {
            unassignDeveloperFromTasksInProject(user, project);
            sendDeveloperUnassignedToProjectNotification(user, project);
            projectAssignmentRepository.deleteByUserIdAndProject(user.id(), project);
        });

        return new ProjectAssignmentResponse(
                request.projectId(),
                users.stream()
                        .map(UserResponse::id)
                        .toList()
        );
    }

    private void unassignDeveloperFromTasksInProject(UserResponse developer, Project project) {
        var tasks = taskClient.getTasksByProjectIdWithDevelopers(project.getId());
        for (var task : tasks) {
            if (task.developers().contains(developer)) {
                taskAssignmentClient.unassignDeveloper(new TaskAssignmentRequest(
                        task.id(),
                        developer.id()
                ));
            }
        }
    }


}
