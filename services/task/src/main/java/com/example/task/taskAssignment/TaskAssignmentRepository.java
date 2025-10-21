package com.example.task.taskAssignment;

import com.example.task.task.Task;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Transactional
public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, Integer> {
    void deleteByTask(Task task);

    Optional<TaskAssignment> findByTaskAndUserId(Task task, Integer userId);

    void deleteByTaskAndUserId(Task task, Integer userId);
}
