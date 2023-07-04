package com.alexsitiy.ideas.project.integration.controller;

import com.alexsitiy.ideas.project.entity.Role;
import com.alexsitiy.ideas.project.integration.IntegrationTestBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;

@AutoConfigureMockMvc
@WithMockUser(username = "test1@gmail.com",password = "123",authorities = "USER")
public class RestIntegrationTestBase extends IntegrationTestBase {

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @SneakyThrows
    public String toJson(Object object){
        return objectMapper.writeValueAsString(object);
    }

    @SneakyThrows
    public <T> T fromJson(String json, Class<T> clazz){
        return objectMapper.readValue(json,clazz);
    }
}
