package com.alexsitiy.ideas.project.listener;

import com.alexsitiy.ideas.project.entity.CommentType;
import com.alexsitiy.ideas.project.entity.Notification;
import com.alexsitiy.ideas.project.entity.User;
import com.alexsitiy.ideas.project.event.ProjectCommentedEvent;
import com.alexsitiy.ideas.project.repository.NotificationRepository;
import com.alexsitiy.ideas.project.repository.ProjectRepository;
import com.alexsitiy.ideas.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class OnProjectCommentedListener {

    private final NotificationRepository notificationRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Async
    @EventListener
    @Transactional
    public void createNotificationForUser(ProjectCommentedEvent event) {
        notificationRepository
                .findByProjectIdAndCallerIdAndType(event.getProjectId(), event.getCallerId(), Notification.NotificationType.COMMENT)
                .ifPresentOrElse(notification -> {
                            if (notification.getComment() == event.getCommentType()) {
                                notificationRepository.delete(notification);
                                notificationRepository.flush();
                                log.debug("Notification: {} was deleted because of User has deleted their comment", notification);
                            } else {
                                String message = notification.getMessage();
                                String newMessage = event.getCommentType() == CommentType.LIKE ?
                                        message.replace("disliked", "liked") :
                                        message.replace("liked", "disliked");

                                notification.setMessage(newMessage);
                                notification.setComment(event.getCommentType());
                                notification.setCreatedAt(Instant.now());
                                notificationRepository.saveAndFlush(notification);
                                log.debug("Notification comment was changed to {} because of User has changed their comment", event.getCommentType());
                            }
                        },
                        () -> projectRepository.findByIdWithUser(event.getProjectId())
                                .ifPresent(project -> {
                                    User caller = userRepository.findById(event.getCallerId()).get();

                                    String message = "Your project [%s] was %s by %s %s"
                                            .formatted(project.getTitle(),
                                                    event.getCommentType() == CommentType.LIKE ? "liked" : "disliked",
                                                    caller.getFirstname(),
                                                    caller.getLastname());

                                    Notification notification = Notification.builder()
                                            .user(project.getUser())
                                            .type(Notification.NotificationType.COMMENT)
                                            .comment(event.getCommentType())
                                            .message(message)
                                            .project(project)
                                            .caller(caller)
                                            .build();

                                    notificationRepository.saveAndFlush(notification);
                                    log.debug("Notification: {} was created", notification);
                                }));
    }
}
