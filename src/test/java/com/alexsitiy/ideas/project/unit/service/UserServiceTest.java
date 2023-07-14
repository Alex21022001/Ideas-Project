package com.alexsitiy.ideas.project.unit.service;

import com.alexsitiy.ideas.project.entity.User;
import com.alexsitiy.ideas.project.repository.UserRepository;
import com.alexsitiy.ideas.project.service.S3Service;
import com.alexsitiy.ideas.project.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class UserServiceTest {

    private static final Integer USER_1_ID = 1;

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private S3Service s3Service;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    void updateAvatar() {
        MockMultipartFile avatar = new MockMultipartFile("testAvatar", "testAvatar.png", "image/png", new byte[123]);
        String newAvatar = "newAvatar.png";
        User user = getUser();

        doReturn(Optional.of(user)).when(userRepository).findById(USER_1_ID);
        doReturn(Optional.of(newAvatar)).when(s3Service).upload(avatar, User.class);

        userService.updateAvatar(USER_1_ID, avatar);

        verify(userRepository, times(1)).saveAndFlush(userCaptor.capture());

        assertThat(userCaptor.getValue()).isNotNull()
                .hasFieldOrPropertyWithValue("id", USER_1_ID)
                .hasFieldOrPropertyWithValue("avatar", newAvatar);
    }

    private User getUser() {
        return User.builder()
                .id(USER_1_ID)
                .build();
    }

}