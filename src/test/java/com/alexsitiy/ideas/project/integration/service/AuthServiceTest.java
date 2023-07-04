package com.alexsitiy.ideas.project.integration.service;

import com.alexsitiy.ideas.project.entity.User;
import com.alexsitiy.ideas.project.integration.IntegrationTestBase;
import com.alexsitiy.ideas.project.repository.UserRepository;
import com.alexsitiy.ideas.project.security.AuthenticationRequest;
import com.alexsitiy.ideas.project.security.AuthenticationResponse;
import com.alexsitiy.ideas.project.security.RegisterRequest;
import com.alexsitiy.ideas.project.service.AuthService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.assertj.core.condition.AllOf;
import org.assertj.core.internal.Conditions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class AuthServiceTest extends IntegrationTestBase {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Test
    void register() {
        String username = "test3@gmail.com";
        RegisterRequest request = new RegisterRequest("Test3","Test3", username,"123");
        AuthenticationResponse expected = authService.register(request);

        entityManager.clear();
        Optional<User> maybeUser = userRepository.findByUsername(username);

        assertThat(maybeUser).isPresent()
                        .map(User::getId).get().isEqualTo(3);
        assertThat(expected).isNotNull()
                .hasFieldOrProperty("token").isNotNull();
    }

    @Test
    void login() {
        AuthenticationRequest request = new AuthenticationRequest("test1@gmail.com","123");

        AuthenticationResponse expected = authService.login(request);

        assertThat(expected).isNotNull()
                .hasFieldOrProperty("token").isNotNull();
    }
}