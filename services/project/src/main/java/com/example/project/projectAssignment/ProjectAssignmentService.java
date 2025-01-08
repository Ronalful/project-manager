package com.example.project.projectAssignment;

import com.example.project.developer.DeveloperClient;
import com.example.project.developer.DeveloperResponse;
import com.example.project.exception.DeveloperNotFoundException;
import com.example.project.exception.ProjectAssignmentExistsException;
import com.example.project.exception.ProjectAssignmentNotExistsException;
import com.example.project.exception.ProjectNotFoundException;
import com.example.project.kafka.ProjectNotification;
import com.example.project.kafka.ProjectOperation;
import com.example.project.kafka.ProjectProducer;
import com.example.project.project.Project;
import com.example.project.project.ProjectRepository;
import com.example.project.task.TaskClient;
import com.example.project.taskAssignment.TaskAssignmentClient;
import com.example.project.taskAssignment.TaskAssignmentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectAssignmentService {

    private final ProjectAssignmentRepository projectAssignmentRepository;
    private final ProjectRepository projectRepository;
    private final DeveloperClient developerClient;
    private final ProjectProducer projectProducer;
    private final TaskClient taskClient;
    private final TaskAssignmentClient taskAssignmentClient;

    public void assignDeveloper(ProjectAssignmentRequest request) {
        var developer = developerClient.getDeveloperById(request.developerId())
                .orElseThrow(() -> new DeveloperNotFoundException("Developer not found with id " + request.developerId()));
        var project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id " + request.projectId()));

        if (isDeveloperAssignedToProject(request.developerId(), project)) {
            throw new ProjectAssignmentExistsException("This developer is already assigned to this project");
        }

        sendDeveloperAssignedToProjectNotification(developer, project);

        projectAssignmentRepository.save(ProjectAssignment.builder()
                        .project(project)
                        .developerId(developer.id())
                        .build());
    }

    private boolean isDeveloperAssignedToProject(Integer developerId, Project project) {
        return projectAssignmentRepository
                .findByProjectAndDeveloperId(project, developerId)
                .isPresent();
    }

    private void sendDeveloperAssignedToProjectNotification(DeveloperResponse developer, Project project) {
        projectProducer.send(new ProjectNotification(
                developer,
                project.getName(),
                ProjectOperation.ASSIGNED
        ));
    }

    private void sendDeveloperUnassignedToProjectNotification(DeveloperResponse developer, Project project) {
        projectProducer.send(new ProjectNotification(
                developer,
                project.getName(),
                ProjectOperation.UNASSIGNED
        ));
    }

    public void unassignDeveloper(ProjectAssignmentRequest request) {
        var project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id " + request.projectId()));
        var developer = developerClient.getDeveloperById(request.developerId())
                .orElseThrow(() -> new DeveloperNotFoundException("Developer not found with id " + request.developerId()));

        if (!isDeveloperAssignedToProject(request.developerId(), project)) {
            throw new ProjectAssignmentNotExistsException("This developer is not assigned to this project");
        }

        unassignDeveloperFromTasksInProject(developer, project);

        sendDeveloperUnassignedToProjectNotification(developer, project);

        projectAssignmentRepository.deleteByDeveloperIdAndProject(request.developerId(), project);
    }

    private void unassignDeveloperFromTasksInProject(DeveloperResponse developer, Project project) {
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
