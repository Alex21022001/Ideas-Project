package com.alexsitiy.ideas.project.integration.service;

import com.alexsitiy.ideas.project.entity.Project;
import com.alexsitiy.ideas.project.entity.ProjectStatus;
import com.alexsitiy.ideas.project.entity.Status;
import com.alexsitiy.ideas.project.entity.User;
import com.alexsitiy.ideas.project.integration.IntegrationTestBase;
import com.alexsitiy.ideas.project.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RequiredArgsConstructor
class EmailServiceIT extends IntegrationTestBase {

    private final EmailService emailService;
    private final JavaMailSender javaMailSender;

    @Captor
    private ArgumentCaptor<MimeMessage> mimeMessageCaptor;


    @Test
    void sendEmail() throws MessagingException {
        String to = "test1@gmai.com";
        String subject = "Test subject";
        String html = "<h1>Test</h1>";

        doNothing().when(javaMailSender).send(any(MimeMessage.class));

        emailService.sendEmail(to, subject, html);

        verify(javaMailSender, times(1)).send(mimeMessageCaptor.capture());
        assertThat(mimeMessageCaptor.getValue().getSubject())
                .isEqualTo(subject);
        assertThat(Arrays.stream(mimeMessageCaptor.getValue().getAllRecipients()))
                .anyMatch(address -> address.toString().equals(to));
    }

    @Test
    void sendStatusNotificationEmail() throws MessagingException {
        Project project = getProject();

        doNothing().when(javaMailSender).send(any(MimeMessage.class));

        emailService.sendStatusNotificationEmail(project);

        verify(javaMailSender, times(1)).send(mimeMessageCaptor.capture());
        assertThat(Arrays.stream(mimeMessageCaptor.getValue().getAllRecipients()))
                .anyMatch(address -> address.toString().equals(project.getUser().getUsername()));
    }

    private Project getProject() {
        return Project.builder()
                .id(1)
                .title("test")
                .description("test desc")
                .imagePath("test.png")
                .status(getProjectStatus())
                .user(getUser())
                .build();
    }

    private User getUser() {
        return User.builder()
                .id(1)
                .firstname("TestUser")
                .lastname("TestLastname")
                .username("test1@gmail.com")
                .build();
    }

    private ProjectStatus getProjectStatus() {
        return ProjectStatus.builder()
                .id(1)
                .status(Status.ACCEPTED)
                .version(1)
                .build();
    }
}