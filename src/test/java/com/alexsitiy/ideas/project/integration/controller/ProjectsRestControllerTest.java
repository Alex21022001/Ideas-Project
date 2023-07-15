package com.alexsitiy.ideas.project.integration.controller;

import com.alexsitiy.ideas.project.dto.ProjectUpdateDto;
import com.alexsitiy.ideas.project.entity.Project;
import com.alexsitiy.ideas.project.entity.Role;
import com.alexsitiy.ideas.project.repository.ProjectRepository;
import com.alexsitiy.ideas.project.security.SecurityUser;
import com.alexsitiy.ideas.project.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RequiredArgsConstructor
class ProjectsRestControllerTest extends RestIntegrationTestBase {

    private static final Integer PROJECT_1_ID = 1;
    
    private final MockMvc mockMvc;
    private final S3Service s3Service;
    private final ProjectRepository projectRepository;

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get("/api/v1/projects")
                        .param("page", "0")
                        .param("size", "2")
                        .param("sort", "likes")
                        .param("title", "test"))
                .andExpect(status().is2xxSuccessful())
                .andExpectAll(
                        jsonPath("data", hasSize(2)),
                        jsonPath("metadata.currentPage").value(0),
                        jsonPath("metadata.totalPages").value(2),
                        jsonPath("metadata.totalElements").value(3));

    }

    @Test
    void findAllByUser() throws Exception {
        mockMvc.perform(get("/api/v1/projects/user")
                        .queryParam("page", "0")
                        .queryParam("size", "1")
                        .queryParam("sort", "dislikes"))
                .andExpect(status().is2xxSuccessful())
                .andExpectAll(
                        jsonPath("data",hasSize(1)),
                        jsonPath("metadata.currentPage").value(0),
                        jsonPath("metadata.totalPages").value(2),
                        jsonPath("metadata.totalElements").value(2)
                );
    }

    @Test
    void findAllLikedProjectsByUser() throws Exception {
        mockMvc.perform(get("/api/v1/projects/user/liked")
                        .queryParam("page", "0")
                        .queryParam("size", "2")
                        .queryParam("sort", "dislikes"))
                .andExpect(status().is2xxSuccessful())
                .andExpectAll(
                        jsonPath("data",hasSize(2)),
                        jsonPath("metadata.currentPage").value(0),
                        jsonPath("metadata.totalPages").value(1),
                        jsonPath("metadata.totalElements").value(2)
                );
    }

    @Test
    void findAllDislikedProjectsByUser() throws Exception {
        mockMvc.perform(get("/api/v1/projects/user/disliked")
                        .queryParam("page", "0")
                        .queryParam("size", "2")
                        .queryParam("sort", "dislikes"))
                .andExpect(status().is2xxSuccessful())
                .andExpectAll(
                        jsonPath("data",hasSize(1)),
                        jsonPath("metadata.currentPage").value(0),
                        jsonPath("metadata.totalPages").value(1),
                        jsonPath("metadata.totalElements").value(1)
                );
    }
    
    @Test
    void getImage() throws Exception {

        doReturn(Optional.of(new byte[1230])).when(s3Service).download(any(),eq(Project.class));

        mockMvc.perform(get("/api/v1/projects/{id}/image",PROJECT_1_ID)
                .accept(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));

    }

    @Test
    void getImageByInvalidProjectId() throws Exception {
        mockMvc.perform(get("/api/v1/projects/{id}/image",-1)
                        .accept(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(status().isNotFound());
    }

    @Test
    void getDoc() throws Exception {

        doReturn(Optional.of(new byte[1230])).when(s3Service).download(any(),eq(Project.class));

        mockMvc.perform(get("/api/v1/projects/{id}/doc",PROJECT_1_ID)
                        .accept(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));
    }

    @Test
    void getDocByInvalidProjectId() throws Exception {
        mockMvc.perform(get("/api/v1/projects/{id}/doc",-1)
                        .accept(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(status().isNotFound());
    }

    @Test
    void create() throws Exception {
        MockMultipartFile imageFile = getImageFile();

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
                        jsonPath("doc", is(nullValue()))
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
        SecurityUser user = getUserDetails();
        String title = "New title";
        String description = "Something";

        ProjectUpdateDto updateDto = new ProjectUpdateDto(title, description);

        mockMvc.perform(put("/api/v1/projects/{id}", PROJECT_1_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(updateDto))
                        .with(user(user)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("id").value(PROJECT_1_ID))
                .andExpect(jsonPath("title").value(title))
                .andExpect(jsonPath("description").value(description));
    }

    @Test
    void updateWithInvalidData() throws Exception {
        SecurityUser user = getUserDetails();
        String title = "";
        String description = RandomStringUtils.random(257);

        ProjectUpdateDto updateDto = new ProjectUpdateDto(title, description);

        mockMvc.perform(put("/api/v1/projects/{id}", PROJECT_1_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(updateDto))
                        .with(user(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations", hasSize(3)));
    }

    @Test
    void updateImage() throws Exception {
        String newImagePath = "newImagePth";
        MockMultipartFile image = getImageFile();

        doReturn(Optional.of(newImagePath)).when(s3Service).upload(image, Project.class);

        mockMvc.perform(multipart("/api/v1/projects/{id}/image", PROJECT_1_ID)
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(getRequestPostProcessor())
                )
                .andExpect(status().is(205));

        Optional<Project> actual = projectRepository.findById(PROJECT_1_ID);
        assertThat(actual).isPresent()
                .map(Project::getImagePath)
                .get().isEqualTo(newImagePath);

    }

    @Test
    void updateInvalidImage() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "image-gif.gif", "image/gif", new byte[]{});

        mockMvc.perform(multipart("/api/v1/projects/{id}/image", PROJECT_1_ID)
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(getRequestPostProcessor())
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateDoc() throws Exception {
        String newDocPath = "newDocPath";
        MockMultipartFile docFile = getDocFile();

        doReturn(Optional.of(newDocPath)).when(s3Service).upload(docFile, Project.class);

        mockMvc.perform(multipart("/api/v1/projects/{id}/doc", PROJECT_1_ID)
                        .file(docFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(getRequestPostProcessor())
                )
                .andExpect(status().is(205));

        Optional<Project> actual = projectRepository.findById(PROJECT_1_ID);
        assertThat(actual).isPresent()
                .map(Project::getDocPath)
                .get().isEqualTo(newDocPath);
    }

    @Test
    void updateInvalidDoc() throws Exception {
        MockMultipartFile image = new MockMultipartFile("doc", "doc-gif.gif", "image/gif", new byte[]{});

        mockMvc.perform(multipart("/api/v1/projects/{id}/doc", PROJECT_1_ID)
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(getRequestPostProcessor())
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/projects/{id}", PROJECT_1_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void likeProject() throws Exception {
        mockMvc.perform(post("/api/v1/projects/{id}/like", PROJECT_1_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void likeNotExistedProject() throws Exception {
        int projectId = -1;

        mockMvc.perform(post("/api/v1/projects/{id}/like", projectId))
                .andExpect(status().isNotFound());
    }

    @Test
    void dislikeProject() throws Exception {
        mockMvc.perform(post("/api/v1/projects/{id}/dislike", PROJECT_1_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void dislikeNotExistedProject() throws Exception {
        int projectId = -1;

        mockMvc.perform(post("/api/v1/projects/{id}/dislike", projectId))
                .andExpect(status().isNotFound());
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
    private MockMultipartFile getImageFile() {
        return new MockMultipartFile("image",
                "some-image.png", "image/png", new byte[123]);
    }


    @NotNull
    private RequestPostProcessor getRequestPostProcessor() {
        return request -> {
            request.setMethod("PUT");
            return request;
        };
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

        @Test
        void updateWithInvalidId() throws Exception {
            int projectId = -1;
            String title = "New title";
            String description = "Something";

            ProjectUpdateDto updateDto = new ProjectUpdateDto(title, description);

            mockMvc.perform(put("/api/v1/projects/{id}", projectId)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(updateDto)))
                    .andExpect(status().isNotFound());
        }

        @Test
        void updateImageWithInvalidUser() throws Exception {
            int projectId = 3;

            mockMvc.perform(multipart("/api/v1/projects/{id}/image", projectId)
                            .file(getImageFile())
                            .contentType(MediaType.MULTIPART_FORM_DATA)
                            .with(getRequestPostProcessor()))
                    .andExpect(status().isForbidden());
        }


        @Test
        void updateDocWithInvalidUser() throws Exception {
            int projectId = 3;

            mockMvc.perform(multipart("/api/v1/projects/{id}/doc", projectId)
                            .file(getDocFile())
                            .contentType(MediaType.MULTIPART_FORM_DATA)
                            .with(getRequestPostProcessor()))
                    .andExpect(status().isForbidden());
        }

        @Test
        void deleteWithInvalidUser() throws Exception {
            int projectId = 3;

            mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/projects/{id}", projectId))
                    .andExpect(status().isForbidden());
        }
    }
}