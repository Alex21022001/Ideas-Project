package com.alexsitiy.ideas.project.integration.controller;

import com.alexsitiy.ideas.project.dto.UserUpdateDto;
import com.alexsitiy.ideas.project.entity.User;
import com.alexsitiy.ideas.project.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RequiredArgsConstructor
class UserRestControllerTest extends RestIntegrationTestBase {

    private static final Integer USER_1_ID = 1;

    private final MockMvc mockMvc;

    private final S3Service s3Service;

    @Test
    void getAuthUser() throws Exception {
        mockMvc.perform(get("/api/v1/users/current"))
                .andExpect(status().is2xxSuccessful())
                .andExpectAll(
                        jsonPath("id").value(1)
                );
    }

    @Test
    void update() throws Exception {
        UserUpdateDto updateDto = new UserUpdateDto("newFirstname", "newLastname");

        mockMvc.perform(put("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(updateDto)))
                .andExpect(status().is2xxSuccessful())
                .andExpectAll(
                        jsonPath("id").value(USER_1_ID),
                        jsonPath("firstname").value(updateDto.getFirstname()),
                        jsonPath("lastname").value(updateDto.getLastname())
                );
    }

    @Test
    void updateWithInvalidData() throws Exception {
        UserUpdateDto updateDto = new UserUpdateDto("", "1");

        mockMvc.perform(put("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(updateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("violations", hasSize(3)));
    }

    @Test
    void updateAvatar() throws Exception {
        MockMultipartFile file = getFile();
        String newAvatar = "newAvatar.png";

        doReturn(Optional.of(newAvatar)).when(s3Service).upload(file, User.class);

        mockMvc.perform(multipart("/api/v1/users/avatar")
                        .file(file)
                        .with(getPutPostProcessor()))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateAvatarWithInvalidFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("avatar", "testAvatar.pdf", "application/pdf", new byte[]{});

        mockMvc.perform(multipart("/api/v1/users/avatar")
                        .file(file)
                        .with(getPutPostProcessor()))
                .andExpect(status().isBadRequest())
                .andExpectAll(jsonPath("violations", hasSize(1)));
    }

    @Test
    @WithAnonymousUser
    void getAvatar() throws Exception {
        doReturn(Optional.of(new byte[123])).when(s3Service).download(anyString(), eq(User.class));

        mockMvc.perform(get("/api/v1/users/{id}/avatar", USER_1_ID)
                        .accept(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));
    }


    @NotNull
    private MockMultipartFile getFile() {
        return new MockMultipartFile("avatar", "testAvatar.png", "image/png", new byte[123]);
    }

    @NotNull
    private RequestPostProcessor getPutPostProcessor() {
        return request -> {
            request.setMethod("PUT");
            return request;
        };
    }

    @Nested
    class AuthUserControllerTest {

        @Test
        @WithAnonymousUser
        void getAuthUserByAnonymous() throws Exception {
            mockMvc.perform(get("/api/v1/users/current"))
                    .andExpect(status().isForbidden());
        }
    }
}