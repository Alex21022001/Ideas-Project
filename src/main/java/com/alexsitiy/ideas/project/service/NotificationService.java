package com.alexsitiy.ideas.project.service;

import com.alexsitiy.ideas.project.entity.*;
import com.alexsitiy.ideas.project.event.ProjectEstimationEvent;
import com.alexsitiy.ideas.project.repository.NotificationRepository;
import com.alexsitiy.ideas.project.repository.ProjectRepository;
import com.alexsitiy.ideas.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createNotificationOnEstimation(Integer projectId, Integer callerId, Status status) {
        projectRepository.findByIdWithUser(projectId)
                .ifPresent(project -> {
                    User user = project.getUser();
                    User caller = userRepository.findById(callerId).orElseThrow();
                    String message = buildMessage(
                            project.getTitle(),
                            status.name(),
                            caller.getFirstname(),
                            caller.getLastname());

                    Notification notification = Notification.builder()
                            .user(user)
                            .project(project)
                            .caller(caller)
                            .message(message)
                            .type(Notification.NotificationType.STATUS)
                            .status(status)
                            .build();

                    notificationRepository.saveAndFlush(notification);
                    log.debug("Notification: {} was created", notification);
                });
    }

    @Transactional
    public void createNotificationOnComment(Integer projectId, Integer callerId, CommentType type) {
        notificationRepository
                .findByProjectIdAndCallerIdAndType(projectId, callerId, Notification.NotificationType.COMMENT)
                .ifPresentOrElse(
                        notification -> {
                            if (notification.getComment() == type) {
                                notificationRepository.delete(notification);
                                notificationRepository.flush();
                                log.debug("Notification: {} was deleted because of User has deleted their comment", notification);
                            } else {
                                String message = notification.getMessage();
                                String newMessage = type == CommentType.LIKE ?
                                        message.replace("disliked", "liked") :
                                        message.replace("liked", "disliked");

                                notification.setMessage(newMessage);
                                notification.setComment(type);
                                notification.setCreatedAt(Instant.now());
                                notificationRepository.saveAndFlush(notification);
                                log.debug("Notification comment was changed to {} because of User has changed their comment", type);
                            }
                        },
                        () -> projectRepository.findByIdWithUser(projectId)
                                .ifPresent(project -> {
                                    User user = project.getUser();
                                    User caller = userRepository.findById(callerId).orElseThrow();

                                    String message = buildMessage(
                                            project.getTitle(),
                                            type == CommentType.LIKE ? "liked" : "disliked",
                                            caller.getFirstname(),
                                            caller.getLastname());

                                    Notification notification = Notification.builder()
                                            .user(user)
                                            .project(project)
                                            .caller(caller)
                                            .message(message)
                                            .type(Notification.NotificationType.COMMENT)
                                            .comment(type)
                                            .build();

                                    notificationRepository.saveAndFlush(notification);
                                    log.debug("Notification: {} was created", notification);
                                }));
    }

    private String buildMessage(String projectTitle, String action, String firstname, String lastname) {
        return "Your project [%s] was %s by %s %s"
                .formatted(projectTitle, action, firstname, lastname);
    }
}
