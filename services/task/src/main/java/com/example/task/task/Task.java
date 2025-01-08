package com.example.task.task;

import com.example.task.taskAssignment.TaskAssignment;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer projectId;
    @OneToMany(mappedBy = "task")
    private List<TaskAssignment> assignments;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TaskPriority priority = TaskPriority.NONE;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TaskStatus status = TaskStatus.NOT_STARTED;
    private Date dueDate;
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updatedAt;

    public void update(TaskUpdateRequest request) {
        if (request.title() != null) title = request.title();
        if (request.description() != null) description = request.description();
        if (request.priority() != null) priority = request.priority();
        if (request.status() != null) status = request.status();
        if (request.dueDate() != null) dueDate = request.dueDate();
    }
}
