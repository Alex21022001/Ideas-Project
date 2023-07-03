package com.alexsitiy.ideas.project.service;

import com.alexsitiy.ideas.project.entity.Role;
import com.alexsitiy.ideas.project.entity.User;
import com.alexsitiy.ideas.project.repository.UserRepository;
import com.alexsitiy.ideas.project.security.AuthenticationResponse;
import com.alexsitiy.ideas.project.security.RegisterRequest;
import com.alexsitiy.ideas.project.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtUtil;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request){
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);

        String token = jwtUtil.generateToken(savedUser);

        return AuthenticationResponse.builder()
                .token(token)
                .type("Bearer ")
                .build();
    }
}
