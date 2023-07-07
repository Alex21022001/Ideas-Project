package com.alexsitiy.ideas.project.integration.controller;

import com.alexsitiy.ideas.project.dto.ProjectUpdateDto;
import com.alexsitiy.ideas.project.entity.Role;
import com.alexsitiy.ideas.project.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.hamcrest.CoreMatchers;
import org.hamcrest.core.Is;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;

import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
class ProjectsRestControllerTest extends RestIntegrationTestBase {

    private final MockMvc mockMvc;

    @Test
    void create() {
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

    @NotNull
    private SecurityUser getUserDetails() {
        return new SecurityUser(1, "test1@gmail.com", null, Collections.singleton(Role.USER));
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