package com.example.task.comment;

import com.example.task.task.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Comment toComment(CommentRequest request, Task task, Integer userId);

    @Mapping(target = "taskId", source = "task.id")
    CommentResponse fromComment(Comment comment);
}
