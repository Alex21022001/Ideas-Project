package com.alexsitiy.ideas.project.listener;

import com.alexsitiy.ideas.project.event.ProjectCommentUpdatedEvent;
import com.alexsitiy.ideas.project.event.ProjectCommentedEvent;
import com.alexsitiy.ideas.project.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OnProjectCommentUpdatedListener {

    private final NotificationService notificationService;

    @Async
    @EventListener
    public void createNotificationForUser(ProjectCommentUpdatedEvent event) {
        notificationService.updateNotificationOnCommentUpdated(event.getProjectId(), event.getCallerId(), event.getCommentType());
    }
}
