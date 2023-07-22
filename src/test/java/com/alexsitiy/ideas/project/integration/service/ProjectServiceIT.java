package com.alexsitiy.ideas.project.integration.service;

import com.alexsitiy.ideas.project.dto.*;
import com.alexsitiy.ideas.project.entity.*;
import com.alexsitiy.ideas.project.integration.IntegrationTestBase;
import com.alexsitiy.ideas.project.repository.ProjectRepository;
import com.alexsitiy.ideas.project.service.ProjectService;
import com.alexsitiy.ideas.project.service.S3Service;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
class ProjectServiceIT extends IntegrationTestBase {

    private static final Integer PROJECT_ID = 1;
    private static final Integer NEXT_PROJECT_ID = 4;
    private static final Integer USER_1_ID = 1;

    private final ProjectService projectService;
    private final S3Service s3Service;

    private final ProjectRepository projectRepository;
    private final EntityManager entityManager;

    @Test
    void create() {
        ProjectCreateDto createDto = getCreateDto();
        String imagePath = "newPath.png";

        doReturn(Optional.of(imagePath)).when(s3Service).upload(any(), eq(Project.class));

        Optional<ProjectReadDto> actual = projectService.create(createDto, USER_1_ID);

        assertThat(actual).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("id", NEXT_PROJECT_ID)
                .hasFieldOrPropertyWithValue("image", imagePath)
                .hasFieldOrPropertyWithValue("status", Status.IN_PROGRESS)
                .extracting("creator", as(InstanceOfAssertFactories.type(UserReadDto.class)))
                .isNotNull().hasFieldOrPropertyWithValue("id", USER_1_ID);

        entityManager.clear();

        Optional<Project> projectWithReaction = projectRepository.findById(NEXT_PROJECT_ID);
        assertThat(projectWithReaction)
                .isPresent().map(Project::getReaction).get()
                .hasFieldOrPropertyWithValue("likes", 0)
                .hasFieldOrPropertyWithValue("dislikes", 0);
    }

    @Test
    void update() {
        String title = "New title";
        String description = "Something";

        Optional<ProjectReadDto> actual = projectService.update(PROJECT_ID, new ProjectUpdateDto(title, description));

        assertThat(actual).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("id", PROJECT_ID)
                .hasFieldOrPropertyWithValue("title", title)
                .hasFieldOrPropertyWithValue("description", description);
    }

    @Test
    void likeProject() {
        int projectId = 3;
        int userId = 1;
        projectService.likeProject(projectId, userId);

        entityManager.clear();

        Optional<Project> project = projectRepository.findById(projectId);
        assertThat(project).isPresent()
                .map(Project::getComments)
                .isNotEmpty().get(InstanceOfAssertFactories.list(Comment.class))
                .anyMatch(comment -> comment.getUser().getId().equals(userId) &&
                                     comment.getType().equals(CommentType.LIKE));
        assertThat(project).isPresent()
                .map(Project::getReaction)
                .isNotEmpty().get(InstanceOfAssertFactories.type(ProjectReaction.class))
                .hasFieldOrPropertyWithValue("likes", 1)
                .hasFieldOrPropertyWithValue("dislikes", 1);
    }

    @Test
    void updateImage() {
        MockMultipartFile imageFile = getImageFile();
        String newImagePath = "newImagePath";

        doReturn(Optional.of(newImagePath)).when(s3Service).upload(imageFile, Project.class);

        projectService.updateImage(PROJECT_ID, imageFile);
        entityManager.clear();

        Optional<Project> project = projectRepository.findById(PROJECT_ID);
        assertThat(project).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("imagePath", newImagePath);

    }

    @Test
    void updateDoc() {
        MockMultipartFile docFile = getDocFile();
        String newDocPath = "newDocPath";

        doReturn(Optional.of(newDocPath)).when(s3Service).upload(docFile, Project.class);

        projectService.updateDoc(PROJECT_ID, docFile);
        entityManager.clear();

        Optional<Project> project = projectRepository.findById(PROJECT_ID);
        assertThat(project).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("docPath", newDocPath);

    }

    @Test
    void acceptProject() {
        projectService.acceptProject(PROJECT_ID);

        Optional<Project> project = projectRepository.findById(PROJECT_ID);
        entityManager.clear();

        assertThat(project).isPresent()
                .map(Project::getStatus)
                .get()
                .hasFieldOrPropertyWithValue("status", Status.ACCEPTED)
                .hasFieldOrPropertyWithValue("version", 1);
    }

    @Test
    void rejectProject() {
        projectService.rejectProject(PROJECT_ID);

        Optional<Project> project = projectRepository.findById(PROJECT_ID);
        entityManager.clear();

        assertThat(project).isPresent()
                .map(Project::getStatus)
                .get()
                .hasFieldOrPropertyWithValue("status", Status.REJECTED)
                .hasFieldOrPropertyWithValue("version", 1);
    }

    @NotNull
    private MockMultipartFile getDocFile() {
        return new MockMultipartFile("doc", "new-doc-file.pdf", "application/pdf", new byte[123]);
    }

    @NotNull
    private MockMultipartFile getImageFile() {
        return new MockMultipartFile("image",
                "some-image.png", "image/png", new byte[123]);
    }

    @NotNull
    private ProjectCreateDto getCreateDto() {
        return new ProjectCreateDto("New Title", "New Description",
                new MockMultipartFile("imageFIle", "some-image.png", "image/png", new byte[123]),
                new MockMultipartFile("empty", new byte[]{}));
    }

}