package com.alexsitiy.ideas.project.integration.service;

import com.alexsitiy.ideas.project.entity.CommentNotification;
import com.alexsitiy.ideas.project.entity.CommentType;
import com.alexsitiy.ideas.project.entity.Status;
import com.alexsitiy.ideas.project.entity.StatusNotification;
import com.alexsitiy.ideas.project.integration.IntegrationTestBase;
import com.alexsitiy.ideas.project.repository.CommentNotificationRepository;
import com.alexsitiy.ideas.project.repository.StatusNotificationRepository;
import com.alexsitiy.ideas.project.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class NotificationServiceIT extends IntegrationTestBase {

    private static final Integer PROJECT_1_ID = 1;
    private static final Integer USER_1_ID = 1;
    private static final Integer USER_2_ID = 2;

    private final NotificationService notificationService;
    private final StatusNotificationRepository statusNotificationRepository;
    private final CommentNotificationRepository commentNotificationRepository;


    @Test
    void createNotificationOnEstimation() {
        notificationService.createNotificationOnEstimation(PROJECT_1_ID, USER_2_ID, Status.ACCEPTED);

        Optional<StatusNotification> actual = statusNotificationRepository.findByProjectIdAndCallerId(PROJECT_1_ID, USER_2_ID);
        assertThat(actual).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("status", Status.ACCEPTED)
                .hasFieldOrProperty("message").isNotNull();
    }

    @Test
    void createNotificationOnCommentCreated() {
        notificationService.createNotificationOnCommentCreated(PROJECT_1_ID, USER_1_ID, CommentType.LIKE);

        Optional<CommentNotification> actual = commentNotificationRepository.findByProjectIdAndCallerId(PROJECT_1_ID, USER_1_ID);
        assertThat(actual).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("commentType", CommentType.LIKE)
                .hasFieldOrProperty("message").isNotNull();
    }

    @Test
    void updateNotificationOnCommentUpdated() {
        notificationService.updateNotificationOnCommentUpdated(PROJECT_1_ID, USER_2_ID, CommentType.DISLIKE);

        Optional<CommentNotification> actual = commentNotificationRepository.findByProjectIdAndCallerId(PROJECT_1_ID, USER_2_ID);
        assertThat(actual).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("commentType", CommentType.DISLIKE)
                .hasFieldOrProperty("message").isNotNull();
    }

    @Test
    void updateNotificationOnCommentUpdated_ifNotificationDoesNotExist() {
        notificationService.updateNotificationOnCommentUpdated(PROJECT_1_ID, USER_1_ID, CommentType.DISLIKE);

        Optional<CommentNotification> actual = commentNotificationRepository.findByProjectIdAndCallerId(PROJECT_1_ID, USER_1_ID);
        assertThat(actual).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("commentType", CommentType.DISLIKE)
                .hasFieldOrProperty("message").isNotNull();
    }

    @Test
    void deleteNotificationOnCommentDeleted() {
        notificationService.deleteNotificationOnCommentDeleted(PROJECT_1_ID, USER_2_ID);

        Optional<CommentNotification> actual = commentNotificationRepository.findByProjectIdAndCallerId(PROJECT_1_ID, USER_2_ID);
        assertThat(actual).isEmpty();
    }

}