package com.alexsitiy.ideas.project.service;

import com.alexsitiy.ideas.project.dto.NotificationReadDto;
import com.alexsitiy.ideas.project.entity.*;
import com.alexsitiy.ideas.project.mapper.NotificationReadMapper;
import com.alexsitiy.ideas.project.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final StatusNotificationRepository statusNotificationRepository;
    private final CommentNotificationRepository commentNotificationRepository;

    private final NotificationReadMapper notificationReadMapper;

    @Transactional
    public void createNotificationOnEstimation(Integer projectId, Integer callerId, Status status) {
        create(projectId, callerId, status);
    }

    @Transactional
    public void createNotificationOnCommentCreated(Integer projectId, Integer callerId, CommentType type) {
        create(projectId, callerId, type);
    }

    @Transactional
    public void updateNotificationOnCommentUpdated(Integer projectId, Integer callerId, CommentType type) {
        commentNotificationRepository.findByProjectIdAndCallerId(projectId, callerId)
                .ifPresentOrElse(notification -> {
                            String message = notification.getMessage();
                            String newMessage = type == CommentType.LIKE ?
                                    message.replace("disliked", "liked") :
                                    message.replace("liked", "disliked");

                            notification.setMessage(newMessage);
                            notification.setCommentType(type);
                            notification.setCreatedAt(Instant.now());
                            commentNotificationRepository.saveAndFlush(notification);
                            log.debug("Notification comment was changed to {} because of User has changed their comment", type);
                        },
                        () -> create(projectId, callerId, type));
    }

    @Transactional
    public void deleteNotificationOnCommentDeleted(Integer projectId, Integer callerId) {
        commentNotificationRepository.findByProjectIdAndCallerId(projectId, callerId)
                .ifPresent(notification -> {
                    commentNotificationRepository.delete(notification);
                    commentNotificationRepository.flush();
                    log.debug("Notification: {} was deleted, due to User with ID: {} deleted their comment", notification, callerId);
                });
    }

    private Notification create(Integer projectId, Integer callerId, Object type) {
        return projectRepository.findByIdWithUser(projectId)
                .map(project -> {
                    User user = project.getUser();
                    User caller = userRepository.findById(callerId).orElseThrow();
                    if (type instanceof Status status) {
                        return createStatusNotification(user, project, caller, status);
                    } else if (type instanceof CommentType commentType) {
                        return createCommentNotification(user, project, caller, commentType);
                    } else {
                        throw new IllegalArgumentException();
                    }
                }).orElseThrow();
    }

    @Transactional
    public void makeNotificationsStale(List<Integer> ids, Integer userId) {
        int count = notificationRepository.makeNotificationsStale(ids, userId);
        log.debug("{} notifications made deprecated", count);
    }

    private StatusNotification createStatusNotification(User user, Project project, User caller, Status status) {
        String message = buildMessage(
                project.getTitle(),
                status.name(),
                caller.getFirstname(),
                caller.getLastname());

        StatusNotification statusNotification = new StatusNotification();
        statusNotification.setProject(project);
        statusNotification.setUser(user);
        statusNotification.setCaller(caller);
        statusNotification.setStatus(status);
        statusNotification.setMessage(message);

        statusNotificationRepository.save(statusNotification);
        log.debug("Notification: {} was created", statusNotification);
        return statusNotification;
    }

    private CommentNotification createCommentNotification(User user, Project project, User caller, CommentType type) {
        String message = buildMessage(
                project.getTitle(),
                type == CommentType.LIKE ? "liked" : "disliked",
                caller.getFirstname(),
                caller.getLastname());

        CommentNotification commentNotification = new CommentNotification();
        commentNotification.setUser(user);
        commentNotification.setProject(project);
        commentNotification.setCaller(caller);
        commentNotification.setMessage(message);
        commentNotification.setCommentType(type);

        commentNotificationRepository.save(commentNotification);
        log.debug("Notification: {} was created", commentNotification);
        return commentNotification;
    }

    private String buildMessage(String projectTitle, String action, String firstname, String lastname) {
        return "Your project [%s] was %s by %s %s"
                .formatted(projectTitle, action, firstname, lastname);
    }

    public Page<NotificationReadDto> findAllByUser(Integer id, Pageable pageable) {
        return notificationRepository.findAllByUserId(id, pageable)
                .map(notificationReadMapper::map);
    }
}
