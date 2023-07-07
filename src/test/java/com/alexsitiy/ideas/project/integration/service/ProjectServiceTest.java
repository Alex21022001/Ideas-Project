package com.alexsitiy.ideas.project.integration.service;

import com.alexsitiy.ideas.project.dto.ProjectCreateDto;
import com.alexsitiy.ideas.project.dto.ProjectReadDto;
import com.alexsitiy.ideas.project.dto.ProjectUpdateDto;
import com.alexsitiy.ideas.project.dto.UserReadDto;
import com.alexsitiy.ideas.project.entity.Project;
import com.alexsitiy.ideas.project.integration.IntegrationTestBase;
import com.alexsitiy.ideas.project.service.ProjectService;
import com.alexsitiy.ideas.project.service.S3Service;
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
    @SpyBean
    private S3Service s3Service;

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

    @NotNull
    private ProjectCreateDto getCreateDto() {
        return new ProjectCreateDto("New Title", "New Description",
                new MockMultipartFile("imageFIle", "some-image.png", "image/png", new byte[123]),
                new MockMultipartFile("empty", new byte[]{}));
    }

}