package com.example.project.projectAssignment;

import com.example.project.project.Project;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Transactional
public interface ProjectAssignmentRepository extends JpaRepository<ProjectAssignment, Integer> {
    void deleteByDeveloperIdAndProject(Integer developerId, Project project);

    Optional<ProjectAssignment> findByProjectAndDeveloperId(Project project, Integer developerId);

    void deleteByProject(Project project);
}
