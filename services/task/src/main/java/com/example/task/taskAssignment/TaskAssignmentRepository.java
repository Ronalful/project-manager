package com.example.task.taskAssignment;

import com.example.task.task.Task;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Transactional
public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, Integer> {
    void deleteByTask(Task task);

    Optional<TaskAssignment> findByTaskAndDeveloperId(Task task, Integer developerId);

    void deleteByTaskAndDeveloperId(Task task, Integer developerId);
}
