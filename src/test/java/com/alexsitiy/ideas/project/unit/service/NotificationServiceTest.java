package com.alexsitiy.ideas.project.unit.service;

import com.alexsitiy.ideas.project.entity.*;
import com.alexsitiy.ideas.project.repository.CommentNotificationRepository;
import com.alexsitiy.ideas.project.repository.ProjectRepository;
import com.alexsitiy.ideas.project.repository.StatusNotificationRepository;
import com.alexsitiy.ideas.project.repository.UserRepository;
import com.alexsitiy.ideas.project.service.NotificationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    private static final Integer PROJECT_1_ID = 1;
    private static final Integer USER_1_ID = 1;
    private static final Integer USER_2_ID = 2;

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private StatusNotificationRepository statusNotificationRepository;
    @Mock
    private CommentNotificationRepository commentNotificationRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<StatusNotification> statusNotificationCaptor;
    @Captor
    private ArgumentCaptor<CommentNotification> commentNotificationCaptor;

    @Test
    void createNotificationOnEstimation() {
        Project project = getProject(PROJECT_1_ID);
        User caller = getCaller(USER_2_ID);

        doReturn(Optional.of(project)).when(projectRepository).findByIdWithUser(PROJECT_1_ID);
        doReturn(Optional.of(caller)).when(userRepository).findById(USER_2_ID);

        notificationService.createNotificationOnEstimation(PROJECT_1_ID, USER_2_ID, Status.ACCEPTED);

        verify(statusNotificationRepository, times(1)).save(statusNotificationCaptor.capture());
        assertThat(statusNotificationCaptor.getValue()).isNotNull()
                .hasFieldOrPropertyWithValue("project", project)
                .hasFieldOrPropertyWithValue("user", getUser())
                .hasFieldOrPropertyWithValue("caller", caller)
                .hasFieldOrProperty("message")
                .hasFieldOrPropertyWithValue("status", Status.ACCEPTED);
    }

    @Test
    void createNotificationOnCommentCreated() {
        Project project = getProject(PROJECT_1_ID);
        User caller = getCaller(USER_2_ID);

        doReturn(Optional.of(project)).when(projectRepository).findByIdWithUser(PROJECT_1_ID);
        doReturn(Optional.of(caller)).when(userRepository).findById(USER_2_ID);

        notificationService.createNotificationOnCommentCreated(PROJECT_1_ID, USER_2_ID, CommentType.LIKE);

        verify(commentNotificationRepository, times(1)).save(commentNotificationCaptor.capture());
        assertThat(commentNotificationCaptor.getValue()).isNotNull()
                .hasFieldOrPropertyWithValue("project", project)
                .hasFieldOrPropertyWithValue("user", getUser())
                .hasFieldOrPropertyWithValue("caller", caller)
                .hasFieldOrProperty("message")
                .hasFieldOrPropertyWithValue("commentType", CommentType.LIKE);
    }

    @Test
    void updateNotificationOnCommentUpdated() {
        CommentNotification commentNotification = getCommentNotification();
        String newMessage = "Your Project: [test1] was disliked by TestExpert TestExpert";

        doReturn(Optional.of(commentNotification))
                .when(commentNotificationRepository)
                .findByProjectIdAndCallerId(PROJECT_1_ID, USER_2_ID);

        notificationService.updateNotificationOnCommentUpdated(PROJECT_1_ID, USER_2_ID, CommentType.DISLIKE);

        verify(commentNotificationRepository, times(1)).saveAndFlush(commentNotificationCaptor.capture());
        assertThat(commentNotificationCaptor.getValue()).isNotNull()
                .hasFieldOrPropertyWithValue("commentType", CommentType.DISLIKE)
                .hasFieldOrPropertyWithValue("message", newMessage);
    }

    @Test
    void updateNotificationOnCommentUpdated_ifNotificationDoesNotExist() {
        Project project = getProject(PROJECT_1_ID);
        User caller = getCaller(USER_1_ID);

        doReturn(Optional.empty()).when(commentNotificationRepository)
                .findByProjectIdAndCallerId(any(), any());
        doReturn(Optional.of(project)).when(projectRepository).findByIdWithUser(PROJECT_1_ID);
        doReturn(Optional.of(caller)).when(userRepository).findById(USER_1_ID);

        notificationService.updateNotificationOnCommentUpdated(PROJECT_1_ID, USER_1_ID, CommentType.LIKE);

        verify(commentNotificationRepository, times(1)).save(commentNotificationCaptor.capture());
        verify(commentNotificationRepository, never()).saveAndFlush(any());
        assertThat(commentNotificationCaptor.getValue()).isNotNull()
                .hasFieldOrPropertyWithValue("project", project)
                .hasFieldOrPropertyWithValue("user", getUser())
                .hasFieldOrPropertyWithValue("caller", caller)
                .hasFieldOrProperty("message")
                .hasFieldOrPropertyWithValue("commentType", CommentType.LIKE);
    }

    @Test
    void deleteNotificationOnCommentDeleted() {
        CommentNotification commentNotification = getCommentNotification();

        doReturn(Optional.of(commentNotification))
                .when(commentNotificationRepository)
                .findByProjectIdAndCallerId(PROJECT_1_ID, USER_2_ID);

        notificationService.deleteNotificationOnCommentDeleted(PROJECT_1_ID, USER_2_ID);

        verify(commentNotificationRepository, times(1)).delete(commentNotificationCaptor.capture());
        assertThat(commentNotificationCaptor.getValue()).isNotNull()
                .hasFieldOrPropertyWithValue("commentType", CommentType.LIKE)
                .hasFieldOrPropertyWithValue("user", getUser())
                .hasFieldOrPropertyWithValue("caller", getCaller(USER_2_ID));
    }

    private CommentNotification getCommentNotification() {
        CommentNotification commentNotification = new CommentNotification();
        commentNotification.setId(1);
        commentNotification.setMessage("Your Project: [test1] was liked by TestExpert TestExpert");
        commentNotification.setUser(getUser());
        commentNotification.setProject(getProject(PROJECT_1_ID));
        commentNotification.setCaller(getCaller(USER_2_ID));
        commentNotification.setCommentType(CommentType.LIKE);
        return commentNotification;
    }

    private Project getProject(Integer projectId) {
        Project project = Project.builder()
                .id(projectId)
                .status(ProjectStatus.builder()
                        .id(projectId)
                        .expert(null)
                        .status(Status.IN_PROGRESS)
                        .version(0)
                        .build())
                .user(getUser())
                .title("Test-title")
                .description("Test-desc")
                .imagePath("image.png")
                .docPath("doc.png")
                .build();
        ProjectReaction projectReaction = new ProjectReaction();
        projectReaction.setProject(project);
        return project;
    }

    private User getCaller(Integer callerId) {
        return User.builder()
                .id(callerId)
                .firstname("TestExpert")
                .lastname("TestExpert")
                .username("test2@gmail.com")
                .build();
    }

    private User getUser() {
        return User.builder()
                .id(1)
                .firstname("Test")
                .lastname("Test")
                .avatar("default.png")
                .role(Role.USER)
                .username("test1@gmail.com")
                .build();
    }
}