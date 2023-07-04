package com.alexsitiy.ideas.project.integration.controller;

import com.alexsitiy.ideas.project.integration.IntegrationTestBase;
import com.alexsitiy.ideas.project.security.AuthenticationRequest;
import com.alexsitiy.ideas.project.security.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.MatcherAssert;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RequiredArgsConstructor
class AuthRestControllerTest extends RestIntegrationTestBase {

    private static final String BASE_URL = "/auth";

    private final MockMvc mockMvc;

    @Test
    @WithAnonymousUser
    void register() throws Exception {
        RegisterRequest request = new RegisterRequest("Test3", "Test3", "test3@gmail.com", "123");

        mockMvc.perform(post(BASE_URL+"/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().is(201))
                .andExpect(jsonPath("token").isString());
    }

    @Test
    @WithAnonymousUser
    void registerWithInvalidData() throws Exception {
        RegisterRequest request = new RegisterRequest("Te", "Test3", "test1@gmail.com", "");

        mockMvc.perform(post(BASE_URL+"/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.violations", hasSize(3)));
    }

    @Test
    @WithAnonymousUser
    void login() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("test1@gmail.com", "123");

        mockMvc.perform(post("/auth/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.token", is(notNullValue())));
    }

    @Test
    void loginWithInvalidData() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("test1@gmail.com", "*****");

        mockMvc.perform(post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error",is(notNullValue())));
    }
}