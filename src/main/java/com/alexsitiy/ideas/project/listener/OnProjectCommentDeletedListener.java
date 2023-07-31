package com.alexsitiy.ideas.project.listener;

import com.alexsitiy.ideas.project.event.ProjectCommentDeletedEvent;
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
public class OnProjectCommentDeletedListener {

    private final NotificationService notificationService;

    @Async
    @EventListener
    public void deleteNotification(ProjectCommentDeletedEvent event) {
        notificationService.deleteNotificationOnCommentDeleted(event.getProjectId(), event.getCallerId());
    }
}
