package com.alexsitiy.ideas.project.unit.service;

import com.alexsitiy.ideas.project.dto.ProjectCreateDto;
import com.alexsitiy.ideas.project.dto.ProjectReadDto;
import com.alexsitiy.ideas.project.dto.ProjectUpdateDto;
import com.alexsitiy.ideas.project.entity.*;
import com.alexsitiy.ideas.project.mapper.ProjectCreateMapper;
import com.alexsitiy.ideas.project.mapper.ProjectReadMapper;
import com.alexsitiy.ideas.project.repository.CommentRepository;
import com.alexsitiy.ideas.project.repository.ProjectRepository;
import com.alexsitiy.ideas.project.repository.ReactionRepository;
import com.alexsitiy.ideas.project.repository.UserRepository;
import com.alexsitiy.ideas.project.service.ProjectService;
import com.alexsitiy.ideas.project.service.S3Service;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    private static final Integer PROJECT_1_ID = 1;

    @InjectMocks
    private ProjectService projectService;
    @Mock
    private S3Service s3Service;

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ReactionRepository reactionRepository;


    @Mock
    private ProjectReadMapper projectReadMapper;
    @Spy
    private ProjectCreateMapper projectCreateMapper;

    @Captor
    private ArgumentCaptor<Project> projectCaptor;
    @Captor
    private ArgumentCaptor<Comment> commentCaptor;
    @Captor
    private ArgumentCaptor<Reaction> reactionCaptor;


    @Test
    void downloadImage() {
        Project project = getProject();

        doReturn(Optional.of(project)).when(projectRepository).findById(PROJECT_1_ID);
        doReturn(Optional.of(new byte[123])).when(s3Service).download(any(), eq(Project.class));

        Optional<byte[]> actual = projectService.downloadImage(PROJECT_1_ID);

        assertThat(actual).isPresent();
    }

    @Test
    void downloadDocIfItNOTExist() {
        Project project = getProject(PROJECT_1_ID, null);

        doReturn(Optional.of(project)).when(projectRepository).findById(PROJECT_1_ID);

        Optional<byte[]> actual = projectService.downloadDoc(PROJECT_1_ID);

        assertThat(actual).isEmpty();
        verify(s3Service, never()).download(any(), eq(Project.class));
    }


    @Test
    void create() {
        int userId = 1;
        String projectTitle = "newTitle";

        User user = User.builder()
                .id(userId)
                .username("test@gmail.com")
                .password("{bcrypt}$2a$10$4MZmbaXMS4An5Ne0Rq2Fs.9JNJZVOtAKO3yQWOpZI7dKknEvTNGYW")
                .firstname("Test1")
                .lastname("Test1")
                .role(Role.USER)
                .build();
        ProjectCreateDto projectDto = new ProjectCreateDto(
                projectTitle,
                "something",
                new MockMultipartFile("image", "project.png", "image/png", new byte[123]),
                new MockMultipartFile("doc", "project.pdf", "application/pdf", new byte[123])
        );
        ProjectReadDto projectRead = ProjectReadDto.builder()
                .id(4).build();

        doReturn(Optional.of(user)).when(userRepository).findById(userId);
        doReturn(Optional.of("filePath")).when(s3Service).upload(any(), any());
        doReturn(projectRead).when(projectReadMapper).map(any());

        Optional<ProjectReadDto> actual = projectService.create(projectDto, userId);

        assertThat(actual).isPresent()
                .map(ProjectReadDto::getId)
                .get()
                .isEqualTo(4);
        verify(projectRepository, times(1)).save(any());
    }


    @Test
    void update() {
        String title = "New Project";
        String description = "Something";

        ProjectUpdateDto updateDto = new ProjectUpdateDto(title, description);
        Project project = getProject();

        doReturn(Optional.of(project)).when(projectRepository).findByIdWithUserAndReaction(PROJECT_1_ID);
        doReturn(Project.builder()
                .id(PROJECT_1_ID)
                .title(title)
                .description(description)
                .build()).when(projectRepository).saveAndFlush(project);
        doReturn(ProjectReadDto.builder()
                .id(PROJECT_1_ID)
                .title(title)
                .description(description)
                .build()).when(projectReadMapper).map(any());

        Optional<ProjectReadDto> actual = projectService.update(PROJECT_1_ID, updateDto);

        assertThat(actual).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("title", title)
                .hasFieldOrPropertyWithValue("id", PROJECT_1_ID)
                .hasFieldOrPropertyWithValue("description", description);
        verify(projectRepository, times(1)).saveAndFlush(project);
    }

    @Test
    void updateImage() {

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "new-image.png",
                "image/png", new byte[123]);
        String newImagePath = "newImagePath";
        Project project = getProject();

        doReturn(Optional.of(project)).when(projectRepository).findById(PROJECT_1_ID);
        doReturn(Optional.of(newImagePath)).when(s3Service).upload(image, Project.class);

        projectService.updateImage(1, image);

        verify(s3Service, times(1)).upload(image, Project.class);
        verify(projectRepository, times(1)).saveAndFlush(projectCaptor.capture());
        Project actual = projectCaptor.getValue();
        assertThat(actual).isNotNull()
                .hasFieldOrPropertyWithValue("id", PROJECT_1_ID)
                .hasFieldOrPropertyWithValue("imagePath", newImagePath);
    }


    @Test
    void updateDoc() {
        MockMultipartFile doc = new MockMultipartFile(
                "doc",
                "new-doc.pdf",
                "application/pdf", new byte[123]);
        String newDocPath = "newDocPath";
        Project project = getProject();

        doReturn(Optional.of(project)).when(projectRepository).findById(PROJECT_1_ID);
        doReturn(Optional.of(newDocPath)).when(s3Service).upload(doc, Project.class);

        projectService.updateDoc(1, doc);

        verify(s3Service, times(1)).upload(doc, Project.class);
        verify(projectRepository, times(1)).saveAndFlush(projectCaptor.capture());
        Project actual = projectCaptor.getValue();
        assertThat(actual).isNotNull()
                .hasFieldOrPropertyWithValue("id", PROJECT_1_ID)
                .hasFieldOrPropertyWithValue("docPath", newDocPath);
    }

    @Test
    void delete() {
        Project project = getProject();

        doReturn(Optional.of(project)).when(projectRepository).findById(PROJECT_1_ID);

        boolean actual = projectService.delete(PROJECT_1_ID);

        assertThat(actual).isTrue();
        verify(projectRepository, times(1)).delete(projectCaptor.capture());
        assertThat(projectCaptor.getValue())
                .isEqualTo(project);
    }

    @Test
    void likeProjectWithoutComment() {
        int userId = 1;

        Reaction reaction = getReaction(getProject());

        doReturn(true).when(projectRepository).existsById(PROJECT_1_ID);
        doReturn(Optional.of(reaction)).when(reactionRepository).findByIdWithLock(PROJECT_1_ID);
        doReturn(Optional.empty()).when(commentRepository).findCommentByProjectIdAndUserId(PROJECT_1_ID, userId);

        boolean actual = projectService.likeProject(PROJECT_1_ID, userId);

        Assertions.assertTrue(actual);
        verify(commentRepository, times(1)).save(commentCaptor.capture());
        verify(reactionRepository, times(1)).saveAndFlush(reactionCaptor.capture());
        verify(commentRepository, never()).saveAndFlush(any());
        verify(commentRepository, never()).delete(any());
        assertThat(commentCaptor.getValue())
                .isNotNull()
                .hasFieldOrPropertyWithValue("type", CommentType.LIKE);
        assertThat(reactionCaptor.getValue())
                .isNotNull()
                .hasFieldOrPropertyWithValue("likes", 2)
                .hasFieldOrPropertyWithValue("dislikes", 1);

    }

    @Test
    void likeProjectWithLike() {
        int userId = 1;

        Project project = getProject();
        Comment comment = getComment(userId, project, CommentType.LIKE);
        Reaction reaction = getReaction(project);

        doReturn(true).when(projectRepository).existsById(PROJECT_1_ID);
        doReturn(Optional.of(reaction)).when(reactionRepository).findByIdWithLock(PROJECT_1_ID);
        doReturn(Optional.of(comment)).when(commentRepository).findCommentByProjectIdAndUserId(PROJECT_1_ID, userId);

        boolean actual = projectService.likeProject(PROJECT_1_ID, userId);

        Assertions.assertTrue(actual);
        verify(commentRepository, times(1)).delete(commentCaptor.capture());
        verify(reactionRepository, times(1)).save(reactionCaptor.capture());
        verify(commentRepository, Mockito.never()).save(any());
        verify(commentRepository, Mockito.never()).saveAndFlush(any());
        assertThat(commentCaptor.getValue())
                .isNotNull()
                .hasFieldOrPropertyWithValue("type", CommentType.LIKE);
        assertThat(reactionCaptor.getValue())
                .isNotNull()
                .hasFieldOrPropertyWithValue("likes", 0)
                .hasFieldOrPropertyWithValue("dislikes", 1);
    }

    @Test
    void likeProjectWithDislike() {
        int userId = 1;

        Project project = getProject();
        Comment comment = getComment(userId, project, CommentType.DISLIKE);
        Reaction reaction = getReaction(project);

        doReturn(true).when(projectRepository).existsById(PROJECT_1_ID);
        doReturn(Optional.of(reaction)).when(reactionRepository).findByIdWithLock(PROJECT_1_ID);
        doReturn(Optional.of(comment)).when(commentRepository).findCommentByProjectIdAndUserId(PROJECT_1_ID, userId);

        boolean actual = projectService.likeProject(PROJECT_1_ID, userId);

        Assertions.assertTrue(actual);
        verify(commentRepository, times(1)).save(commentCaptor.capture());
        verify(reactionRepository, times(1)).save(reactionCaptor.capture());
        verify(commentRepository, Mockito.never()).saveAndFlush(any());
        verify(commentRepository, Mockito.never()).delete(any());
        assertThat(commentCaptor.getValue())
                .isNotNull()
                .hasFieldOrPropertyWithValue("type", CommentType.LIKE);
        assertThat(reactionCaptor.getValue())
                .isNotNull()
                .hasFieldOrPropertyWithValue("likes", 2)
                .hasFieldOrPropertyWithValue("dislikes", 0);
    }

    @NotNull
    private Comment getComment(int userId, Project project, CommentType commentType) {
        return Comment.of(project,
                User.builder()
                        .id(userId)
                        .build(), commentType);
    }

    private Project getProject() {
        return Project.builder()
                .id(ProjectServiceTest.PROJECT_1_ID)
                .title("test1")
                .description("test1-description")
                .imagePath("test1.png")
                .docPath("test1.pdf")
                .status(Status.WAITING)
                .build();
    }

    private Project getProject(int projectId, String doc) {
        return Project.builder()
                .id(projectId)
                .title("test1")
                .description("test1-description")
                .imagePath("test1.png")
                .docPath(doc)
                .status(Status.WAITING)
                .build();
    }

    private Reaction getReaction(Project project) {
        return new Reaction(1, 1, 1, project);
    }
}











