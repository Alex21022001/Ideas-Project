package com.alexsitiy.ideas.project.integration.service;

import com.alexsitiy.ideas.project.dto.*;
import com.alexsitiy.ideas.project.entity.*;
import com.alexsitiy.ideas.project.integration.IntegrationTestBase;
import com.alexsitiy.ideas.project.repository.ProjectRepository;
import com.alexsitiy.ideas.project.service.ProjectService;
import com.alexsitiy.ideas.project.service.S3Service;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.api.InstanceOfAssertFactory;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
class ProjectServiceTest extends IntegrationTestBase {

    private final ProjectService projectService;
    private final S3Service s3Service;

    private final ProjectRepository projectRepository;
    private final EntityManager entityManager;

    @Test
    void create() {
        int userId = 1;
        ProjectCreateDto createDto = getCreateDto();
        String imagePath = "newPath.png";

        doReturn(Optional.of(imagePath)).when(s3Service).upload(any(), eq(Project.class));

        Optional<ProjectReadDto> actual = projectService.create(createDto, userId);

        assertThat(actual).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("id", 4)
                .hasFieldOrPropertyWithValue("image", imagePath)
                .extracting("creator", as(InstanceOfAssertFactories.type(UserReadDto.class)))
                .isNotNull().hasFieldOrPropertyWithValue("id", userId);
    }

    @Test
    void update() {
        int projectId = 1;
        String title = "New title";
        String description = "Something";

        Optional<ProjectReadDto> actual = projectService.update(projectId, new ProjectUpdateDto(title, description));

        assertThat(actual).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("id", projectId)
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
    }

//    @Test
//    void findAll() {
//        List<Tuple> projects = projectRepository.findAllProjectsWithLikesAndDislikes();
//        projects.stream().map(tuple -> {
//            Project project = tuple.get(0, Project.class);
//            Integer likes = tuple.get(1, Integer.class);
//            Integer dislikes = tuple.get(2, Integer.class);
//            return new ProjectProjection(project, likes, dislikes);
//        }).forEach(System.out::println);
//    }

    @NotNull
    private ProjectCreateDto getCreateDto() {
        return new ProjectCreateDto("New Title", "New Description",
                new MockMultipartFile("imageFIle", "some-image.png", "image/png", new byte[123]),
                new MockMultipartFile("empty", new byte[]{}));
    }

}