package com.alexsitiy.ideas.project.integration.controller;

import com.alexsitiy.ideas.project.dto.ProjectCreateDto;
import com.alexsitiy.ideas.project.dto.ProjectUpdateDto;
import com.alexsitiy.ideas.project.entity.Project;
import com.alexsitiy.ideas.project.entity.Role;
import com.alexsitiy.ideas.project.repository.ProjectRepository;
import com.alexsitiy.ideas.project.security.SecurityUser;
import com.alexsitiy.ideas.project.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.hamcrest.core.Is;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.Part;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
class ProjectsRestControllerTest extends RestIntegrationTestBase {

    private final MockMvc mockMvc;
    private final S3Service s3Service;
    private final ProjectRepository projectRepository;

    @Test
    void create() throws Exception {
        MockMultipartFile imageFile = getImage();

        doReturn(Optional.of("newPath")).when(s3Service).upload(any(), eq(Project.class));

        mockMvc.perform(multipart("/api/v1/projects")
                        .file(imageFile)
                        .param("title", "new Title")
                        .param("description", "Something")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(201))
                .andExpectAll(
                        jsonPath("id").value(4),
                        jsonPath("image", is(notNullValue())),
                        jsonPath("docs", is(nullValue()))
                );
    }

    @Test
    void createWithInvalidData() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile("image",
                "some-image.gif", "image/gif", new byte[123]);

        doReturn(Optional.of("newPath")).when(s3Service).upload(any(), eq(Project.class));

        mockMvc.perform(multipart("/api/v1/projects")
                        .file(imageFile)
                        .param("title", "test1")
                        .param("description", "Something")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpectAll(
                        jsonPath("violations", hasSize(2))
                );
    }

    @Test
    void update() throws Exception {
        int projectId = 1;
        SecurityUser user = getUserDetails();
        String title = "New title";
        String description = "Something";

        ProjectUpdateDto updateDto = new ProjectUpdateDto(title, description);

        mockMvc.perform(put("/api/v1/projects/{id}", projectId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(updateDto))
                        .with(user(user)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("id").value(projectId))
                .andExpect(jsonPath("title").value(title))
                .andExpect(jsonPath("description").value(description));
    }

    @Test
    void updateWithInvalidData() throws Exception {
        int projectId = 1;
        SecurityUser user = getUserDetails();
        String title = "";
        String description = RandomStringUtils.random(257);

        ProjectUpdateDto updateDto = new ProjectUpdateDto(title, description);

        mockMvc.perform(put("/api/v1/projects/{id}", projectId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(updateDto))
                        .with(user(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations", hasSize(3)));
    }

    @Test
    void updateImage() throws Exception {
        int projectId = 1;
        String newImagePath = "newImagePth";
        MockMultipartFile image = getImage();

        doReturn(Optional.of(newImagePath)).when(s3Service).upload(image, Project.class);

        mockMvc.perform(multipart("/api/v1/projects/{id}/image", projectId)
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                )
                .andExpect(status().is(205));

        Optional<Project> actual = projectRepository.findById(projectId);
        assertThat(actual).isPresent()
                .map(Project::getImagePath)
                .get().isEqualTo(newImagePath);

    }

    @Test
    void updateInvalidImage() throws Exception {
        int projectId = 1;
        MockMultipartFile image = new MockMultipartFile("image", "image-gif.gif", "image/gif", new byte[]{});

        mockMvc.perform(multipart("/api/v1/projects/{id}/image", projectId)
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateDoc() throws Exception {
        int projectId = 1;
        String newDocPath = "newDocPath";
        MockMultipartFile docFile = getDocFile();

        doReturn(Optional.of(newDocPath)).when(s3Service).upload(docFile, Project.class);

        mockMvc.perform(multipart("/api/v1/projects/{id}/doc", projectId)
                        .file(docFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                )
                .andExpect(status().is(205));

        Optional<Project> actual = projectRepository.findById(projectId);
        assertThat(actual).isPresent()
                .map(Project::getDocsPath)
                .get().isEqualTo(newDocPath);
    }

    @Test
    void updateInvalidDoc() throws Exception {
        int projectId = 1;
        MockMultipartFile image = new MockMultipartFile("doc", "doc-gif.gif", "image/gif", new byte[]{});

        mockMvc.perform(multipart("/api/v1/projects/{id}/doc", projectId)
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                )
                .andExpect(status().isBadRequest());
    }

    @NotNull
    private MockMultipartFile getDocFile() {
        return new MockMultipartFile("doc", "new-doc-file.pdf", "application/pdf", new byte[123]);
    }

    @NotNull
    private SecurityUser getUserDetails() {
        return new SecurityUser(1, "test1@gmail.com", null, Collections.singleton(Role.USER));
    }

    @NotNull
    private MockMultipartFile getImage() {
        return new MockMultipartFile("image",
                "some-image.png", "image/png", new byte[123]);
    }


    @Nested
    class ProjectRestControllerAuthTest {

        @Test
        void updateWithInvalidUser() throws Exception {
            int projectId = 3;
            String title = "New title";
            String description = "Something";

            SecurityUser user = new SecurityUser(1, "test1@gmail.com", null, Collections.singleton(Role.USER));
            ProjectUpdateDto updateDto = new ProjectUpdateDto(title, description);

            mockMvc.perform(put("/api/v1/projects/{id}", projectId)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(updateDto))
                            .with(user(user)))
                    .andExpect(status().isForbidden());
        }
    }
}