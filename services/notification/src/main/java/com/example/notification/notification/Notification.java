package com.example.notification.notification;

import com.example.notification.kafka.project.ProjectNotification;
import com.example.notification.kafka.task.TaskChangedStateNotification;
import com.example.notification.kafka.task.TaskNotification;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document
public class Notification {
    @Id
    private String id;
    private NotificationType type;
    private LocalDateTime notificationDate;
    private ProjectNotification projectNotification;
    private TaskNotification taskNotification;
    private TaskChangedStateNotification taskChangedStateNotification;
}
