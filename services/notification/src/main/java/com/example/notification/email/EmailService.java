package com.example.notification.email;

import com.example.notification.kafka.project.ProjectNotification;
import com.example.notification.kafka.task.TaskChangedStateNotification;
import com.example.notification.kafka.task.TaskNotification;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    private void sendEmail(
            String destinationEmail,
            Map<String, Object> variables,
            EmailTemplates template
    ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, UTF_8.name());
        messageHelper.setFrom("project@manager.com");

        final String templateName = template.getTemplate();

        Context context = new Context();
        context.setVariables(variables);
        messageHelper.setSubject(template.getSubject());

        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);

            messageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);
            log.info(String.format("INFO - Email successfully sent to %s with template %s ", destinationEmail, templateName));
        } catch (MessagingException e) {
            log.warn("WARNING - Cannot send Email to {} ", destinationEmail);
        }

    }

    public void sendProjectNotificationEmail(ProjectNotification body) throws MessagingException {
        Map<String, Object> variables = new HashMap<>();
        variables.put("developer", body.developer());
        variables.put("projectName", body.projectName());
        variables.put("operation", body.operation());

        sendEmail(body.developer().email(),
                variables,
                EmailTemplates.PROJECT_NOTIFICATION);
    }

    public void sendTaskNotificationEmail(TaskNotification body) throws MessagingException {
        Map<String, Object> variables = new HashMap<>();
        variables.put("developer", body.developer());
        variables.put("taskTitle", body.taskTitle());
        variables.put("project", body.project());
        variables.put("operation", body.operation());

        sendEmail(body.developer().email(),
                variables,
                EmailTemplates.TASK_NOTIFICATION);
    }

    public void sendTaskChangedStateNotificationEmail(TaskChangedStateNotification body) throws MessagingException {
        Map<String, Object> variables = new HashMap<>();
        variables.put("developer", body.developer());
        variables.put("taskTitle", body.taskTitle());
        variables.put("taskStatus", body.taskStatus());
        variables.put("project", body.project());

        sendEmail(body.developer().email(),
                variables,
                EmailTemplates.TASK_CHANGED_STATE_NOTIFICATION);
    }
}
