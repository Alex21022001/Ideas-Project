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
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class AuthServiceIT extends IntegrationTestBase {

    private static final Integer NEXT_USER_ID = 3;

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
                        .map(User::getId).get().isEqualTo(NEXT_USER_ID);
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