package com.alexsitiy.ideas.project.listener;

import com.alexsitiy.ideas.project.event.ProjectEstimateEvent;
import com.alexsitiy.ideas.project.repository.ProjectRepository;
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
public class OnProjectEstimateListener {

    private final ProjectRepository projectRepository;
    private final EmailService emailService;

    @Async
    @EventListener
    @Transactional(readOnly = true)
    public void sendEmailToUser(ProjectEstimateEvent event) {
        projectRepository.findByIdWithUserAndReactionAndStatus(event.getProjectId())
                .ifPresent(emailService::sendStatusNotificationEmail);
    }

}
