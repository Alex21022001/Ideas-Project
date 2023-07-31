package com.alexsitiy.ideas.project.listener;

import com.alexsitiy.ideas.project.event.ProjectEstimationEvent;
import com.alexsitiy.ideas.project.repository.ProjectRepository;
import com.alexsitiy.ideas.project.service.EmailService;
import com.alexsitiy.ideas.project.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OnProjectEstimationListener {

    private final NotificationService notificationService;
    private final ProjectRepository projectRepository;
    private final EmailService emailService;

    @Async
    @EventListener
    @Order(1)
    public void createNotificationForUser(ProjectEstimationEvent event) {
        notificationService.createNotificationOnEstimation(event.getProjectId(), event.getCallerId(), event.getStatus());
    }

    @Async
    @Order(2)
    @EventListener
    @Transactional(readOnly = true)
    public void sendStatusNotificationEmailToUser(ProjectEstimationEvent event) {
        projectRepository.findByIdWithUser(event.getProjectId())
                .ifPresent(emailService::sendStatusNotificationEmail);
    }

}
