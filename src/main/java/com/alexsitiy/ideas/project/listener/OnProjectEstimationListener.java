package com.alexsitiy.ideas.project.listener;

import com.alexsitiy.ideas.project.entity.Notification;
import com.alexsitiy.ideas.project.event.ProjectEstimationEvent;
import com.alexsitiy.ideas.project.repository.NotificationRepository;
import com.alexsitiy.ideas.project.repository.ProjectRepository;
import com.alexsitiy.ideas.project.repository.UserRepository;
import com.alexsitiy.ideas.project.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OnProjectEstimationListener {

    private final NotificationRepository notificationRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Async
    @EventListener
    @Transactional(readOnly = true)
    public void sendEmailToUser(ProjectEstimationEvent event) {
        projectRepository.findByIdWithUser(event.getProjectId())
                .ifPresent(emailService::sendStatusNotificationEmail);
    }

    @Async
    @EventListener
    @Transactional
    public void createNotificationForUser(ProjectEstimationEvent event) {
        projectRepository.findByIdWithUser(event.getProjectId())
                .ifPresent(project -> {
                    String message = "Your project [%s] was %s by Expert %s %s"
                            .formatted(project.getTitle(),
                                    project.getStatus().getStatus(),
                                    project.getStatus().getExpert().getFirstname(),
                                    project.getStatus().getExpert().getLastname());

                    Notification notification = Notification.builder()
                            .message(message)
                            .type(Notification.NotificationType.STATUS)
                            .caller(project.getStatus().getExpert())
                            .project(project)
                            .user(project.getUser())
                            .build();

                    notificationRepository.saveAndFlush(notification);
                    log.debug("Notification: {} was created", notification);
                });
    }

}
