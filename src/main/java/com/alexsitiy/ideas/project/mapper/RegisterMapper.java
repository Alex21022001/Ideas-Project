package com.alexsitiy.ideas.project.mapper;

import com.alexsitiy.ideas.project.entity.Role;
import com.alexsitiy.ideas.project.entity.User;
import com.alexsitiy.ideas.project.security.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class RegisterMapper implements Mapper<RegisterRequest, User> {

    @Value("${app.user.default-avatars}")
    private final List<String> USER_DEFAULT_AVATARS;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User map(RegisterRequest request) {
        return User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .avatar(getDefaultRandomAvatar())
                .build();
    }

    private String getDefaultRandomAvatar() {
        Random random = new Random();
        return USER_DEFAULT_AVATARS.get(random.nextInt(USER_DEFAULT_AVATARS.size()));
    }
}
