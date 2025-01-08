package com.example.notification.kafka;

import com.example.notification.email.EmailService;
import com.example.notification.kafka.project.ProjectNotification;
import com.example.notification.kafka.task.TaskChangedStateNotification;
import com.example.notification.kafka.task.TaskNotification;
import com.example.notification.notification.Notification;
import com.example.notification.notification.NotificationRepository;
import com.example.notification.notification.NotificationType;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationConsumer {
    private final NotificationRepository repository;
    private final EmailService emailService;

    @KafkaListener(topics = "project-topic")
    public void consumeProjectNotification(ProjectNotification projectNotification) throws MessagingException {
        repository.save(
                Notification.builder()
                        .type(NotificationType.PROJECT_ASSIGNMENT)
                        .notificationDate(LocalDateTime.now())
                        .projectNotification(projectNotification)
                        .build()
        );

        emailService.sendProjectNotificationEmail(projectNotification);
    }

    @KafkaListener(topics = "task-topic")
    public void consumeTaskNotification(TaskNotification taskNotification) throws MessagingException {
        repository.save(
                Notification.builder()
                        .type(NotificationType.TASK_ASSIGNMENT)
                        .notificationDate(LocalDateTime.now())
                        .taskNotification(taskNotification)
                        .build()
        );

        emailService.sendTaskNotificationEmail(taskNotification);
    }

    @KafkaListener(topics = "task-changed-state-topic")
    public void consumeTaskChangedStateNotification(TaskChangedStateNotification taskChangedStateNotification) throws MessagingException {
        repository.save(
                Notification.builder()
                        .type(NotificationType.TASK_CHANGED_STATE)
                        .notificationDate(LocalDateTime.now())
                        .taskChangedStateNotification(taskChangedStateNotification)
                        .build()
        );

        emailService.sendTaskChangedStateNotificationEmail(taskChangedStateNotification);
    }
}
