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
                .extracting("creator", as(InstanceOfAssertFactories.type(UserReadDto.class)))
                .isNotNull().hasFieldOrPropertyWithValue("id", USER_1_ID);
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
        boolean actual = projectService.likeProject(projectId, userId);

        assertThat(actual).isTrue();

        entityManager.clear();

        Optional<Project> project = projectRepository.findById(projectId);
        assertThat(project).isPresent()
                .map(Project::getComments)
                .isNotEmpty().get(InstanceOfAssertFactories.list(Comment.class))
                .anyMatch(comment -> comment.getUser().getId().equals(userId) &&
                                     comment.getType().equals(CommentType.LIKE));
        assertThat(project).isPresent()
                .map(Project::getReaction)
                .isNotEmpty().get(InstanceOfAssertFactories.type(Reaction.class))
                .hasFieldOrPropertyWithValue("likes", 1)
                .hasFieldOrPropertyWithValue("dislikes", 1);
    }

    @NotNull
    private ProjectCreateDto getCreateDto() {
        return new ProjectCreateDto("New Title", "New Description",
                new MockMultipartFile("imageFIle", "some-image.png", "image/png", new byte[123]),
                new MockMultipartFile("empty", new byte[]{}));
    }

}