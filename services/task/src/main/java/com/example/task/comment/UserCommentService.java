package com.example.task.comment;

import com.example.task.exception.CommentException;
import com.example.task.exception.CommentNotFoundException;
import com.example.task.exception.TaskNotFoundException;
import com.example.task.project.ProjectResponse;
import com.example.task.project.UserProjectClient;
import com.example.task.task.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCommentService {
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;
    private final UserProjectClient userProjectClient;
    private final CommentMapper mapper;

    public CommentResponse add(CommentRequest request, Integer userId) {
        var task = taskRepository.findByIdAndAssignments_UserId(request.taskId(), userId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id " + request.taskId()));
        var comment = commentRepository.save(mapper.toComment(request, task, userId));
        return mapper.fromComment(comment);
    }

    public CommentResponse update(UpdateCommentRequest request, Long userId) {
        var comment = commentRepository.findById(request.id())
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id " + request.id()));
        if (!comment.getUserId().equals(userId)) {
            throw new CommentException("This comment doesn't belong to this user");
        }
        comment.setText(request.text());
        return mapper.fromComment(commentRepository.save(comment));
    }

    public void delete(Long id, Long userId) {
        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id " + id));
        if (!comment.getUserId().equals(userId)) {
            throw new CommentException("This comment doesn't belong to this user");
        }
        commentRepository.delete(comment);
    }

    public CommentResponse getById(Long userId, Long id) {
        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id " + id));
        var projectIds = userProjectClient.getProjects(userId.toString()).stream()
                .map(ProjectResponse::id)
                .toList();
        if (!projectIds.contains(comment.getTask().getProjectId())) {
            throw new CommentException("This user doesn't belong to this project");
        }
        return mapper.fromComment(comment);
    }

    public List<CommentResponse> getByTaskId(Long userId, Integer taskId) {
        var comments = commentRepository.findByTask_Id(taskId);
        if (comments.isEmpty()) {
            throw new CommentNotFoundException("Comment not found in task " + taskId);
        }
        var projectIds = userProjectClient.getProjects(userId.toString()).stream()
                .map(ProjectResponse::id)
                .toList();
        if (!projectIds.contains(comments.get(0).getTask().getProjectId())) {
            throw new CommentException("This user doesn't belong to this project");
        }
        return comments.stream()
                .map(mapper::fromComment)
                .toList();
    }
}
