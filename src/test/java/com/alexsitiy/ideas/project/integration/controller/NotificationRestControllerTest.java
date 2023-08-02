package com.alexsitiy.ideas.project.integration.controller;

import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
class NotificationRestControllerTest extends RestIntegrationTestBase {

    private final MockMvc mockMvc;

    @Test
    void getUserNotifications() throws Exception {
        mockMvc.perform(get("/api/v1/notifications/user")
                        .queryParam("page", "0")
                        .queryParam("size", "2")
                        .queryParam("sort", "new")
                )
                .andExpect(status().is2xxSuccessful())
                .andExpectAll(
                        jsonPath("data", hasSize(2)),
                        jsonPath("metadata.totalElements").value(3),
                        jsonPath("data[0].id").value(4)
                );

    }
}