package com.example.project.project;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    List<Project> findByAssignments_UserId(Integer userId);

    Optional<Project> findByAssignments_UserIdAndId(Integer userId, Integer id);
}
