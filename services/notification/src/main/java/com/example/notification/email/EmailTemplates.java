package com.example.notification.email;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailTemplates {
    PROJECT_NOTIFICATION("project-notification.html", "Project Notification"),
    TASK_NOTIFICATION("task-notification.html", "Task Notification"),
    TASK_CHANGED_STATE_NOTIFICATION("task-changed-state-notification.html", "Task Changed State Notification"),;

    private final String template;
    private final String subject;
}
