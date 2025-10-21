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
    public void assignUser(ProjectAssignmentRequest request) {
        var user = userClient.getUserById(request.userId())
                .orElseThrow(() -> new DeveloperNotFoundException("Developer not found with id " + request.userId()));

        if (!user.role().equals(Role.USER)) {
            throw new DeveloperIsNotUser("Developer must be user");
        }

        var project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id " + request.projectId()));

        if (isDeveloperAssignedToProject(request.userId(), project)) {
            throw new ProjectAssignmentExistsException("This user is already assigned to this project");
        }

        sendDeveloperAssignedToProjectNotification(user, project);

        projectAssignmentRepository.save(ProjectAssignment.builder()
                        .project(project)
                        .userId(user.id())
                        .build());
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
    public void unassignUser(ProjectAssignmentRequest request) {
        var project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id " + request.projectId()));
        var user = userClient.getUserById(request.userId())
                .orElseThrow(() -> new DeveloperNotFoundException("Developer not found with id " + request.userId()));

        if (!user.role().equals(Role.USER)) {
            throw new DeveloperIsNotUser("Developer must be user");
        }

        if (!isDeveloperAssignedToProject(request.userId(), project)) {
            throw new ProjectAssignmentNotExistsException("This user is not assigned to this project");
        }

        unassignDeveloperFromTasksInProject(user, project);

        sendDeveloperUnassignedToProjectNotification(user, project);

        projectAssignmentRepository.deleteByUserIdAndProject(request.userId(), project);
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
