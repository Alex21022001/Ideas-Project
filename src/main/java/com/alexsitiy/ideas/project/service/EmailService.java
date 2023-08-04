package com.alexsitiy.ideas.project.service;

import com.alexsitiy.ideas.project.entity.Project;
import com.alexsitiy.ideas.project.entity.User;
import jakarta.annotation.Nullable;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private static final String PROJECT_STATUS_NOTIFICATION_HTML = "/templates/project-estimation.html";

    @Value("${spring.mail.username}")
    private final String from;
    @Value("${app.email.subject}")
    private final String baseSubject;

    private final ResourceLoader resourceLoader;

    private final JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String html) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setFrom(from);
        helper.setSubject(subject);
        helper.setText(html, true);

        javaMailSender.send(message);
    }


    public void sendStatusNotificationEmail(Project project) {
        String subject = baseSubject + "- Project Status Notification";
        User user = project.getUser();
        try {
            Resource resource = resourceLoader.getResource("classpath:" + PROJECT_STATUS_NOTIFICATION_HTML);
            String userNotificationTemplate = new String(resource.getContentAsByteArray());
            String userNotificationHtml = userNotificationTemplate
                    .replace("${firstname}", user.getFirstname())
                    .replace("${lastname}", user.getLastname())
                    .replace("${projectTitle}", project.getTitle())
                    .replace("${projectStatus}", project.getStatus().getStatus().name());

            this.sendEmail(user.getUsername(), subject, userNotificationHtml);
            log.debug("Project Status Notification email was successfully sent to {}", user.getUsername());
        } catch (FileNotFoundException e) {
            log.error("HTML template: {} was not found", PROJECT_STATUS_NOTIFICATION_HTML);
        } catch (IOException e) {
            log.error("Couldn't read file: {}", PROJECT_STATUS_NOTIFICATION_HTML);
        } catch (MessagingException e) {
            log.error("Couldn't send Email message to {}. Exception: {}", user.getUsername(), e);
        }
    }
}
