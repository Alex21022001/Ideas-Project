package com.alexsitiy.ideas.project.integration.service;

import com.alexsitiy.ideas.project.dto.UserFullReadDto;
import com.alexsitiy.ideas.project.dto.UserReadDto;
import com.alexsitiy.ideas.project.dto.UserUpdateDto;
import com.alexsitiy.ideas.project.entity.User;
import com.alexsitiy.ideas.project.integration.IntegrationTestBase;
import com.alexsitiy.ideas.project.repository.UserRepository;
import com.alexsitiy.ideas.project.security.SecurityUser;
import com.alexsitiy.ideas.project.service.S3Service;
import com.alexsitiy.ideas.project.service.UserService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class UserServiceIT extends IntegrationTestBase {

    private static final Integer USER_1_ID = 1;
    private static final String USER_1_USERNAME = "test1@gmail.com";

    private final UserService userService;
    private final S3Service s3Service;

    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Test
    void findById() {
        Optional<UserFullReadDto> maybeUser = userService.findById(USER_1_ID);

        assertThat(maybeUser).isPresent()
                .get(InstanceOfAssertFactories.type(UserFullReadDto.class))
                .hasFieldOrPropertyWithValue("id", USER_1_ID)
                .hasFieldOrPropertyWithValue("username", "test1@gmail.com");
    }

    @Test
    void update() {
        UserUpdateDto updateDto = new UserUpdateDto("newFirstname", "newLastname");

        Optional<UserFullReadDto> actual = userService.update(USER_1_ID, updateDto);

        assertThat(actual).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("id", USER_1_ID)
                .hasFieldOrPropertyWithValue("firstname", updateDto.getFirstname())
                .hasFieldOrPropertyWithValue("lastname", updateDto.getLastname());
    }

    @Test
    void updateAvatar() {
        MockMultipartFile avatar = getFile();
        String newAvatar = "newAvatar.png";

        Mockito.doReturn(Optional.of(newAvatar)).when(s3Service).upload(avatar, User.class);

        userService.updateAvatar(USER_1_ID, avatar);
        entityManager.clear();

        Optional<User> actual = userRepository.findById(USER_1_ID);
        assertThat(actual).isPresent()
                .get(InstanceOfAssertFactories.type(User.class))
                .hasFieldOrPropertyWithValue("id", USER_1_ID)
                .hasFieldOrPropertyWithValue("avatar", newAvatar);
    }

    @Test
    void getAvatar() {
        Mockito.doReturn(Optional.of(new byte[123])).when(s3Service).download("test1Avatar.png", User.class);

        Optional<byte[]> actual = userService.getAvatar(USER_1_ID);

        assertThat(actual).isPresent();
    }

    @Test
    void loadUserByUsername() {
        UserDetails userDetails = userService.loadUserByUsername(USER_1_USERNAME);

        assertThat(userDetails)
                .hasFieldOrPropertyWithValue("username", USER_1_USERNAME);
    }

    @NotNull
    private MockMultipartFile getFile() {
        return new MockMultipartFile("testAvatar", "testAvatar.png", "image/png", new byte[123]);
    }
}