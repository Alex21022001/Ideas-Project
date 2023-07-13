package com.alexsitiy.ideas.project.service;

import com.alexsitiy.ideas.project.entity.Role;
import com.alexsitiy.ideas.project.entity.User;
import com.alexsitiy.ideas.project.repository.UserRepository;
import com.alexsitiy.ideas.project.security.AuthenticationRequest;
import com.alexsitiy.ideas.project.security.AuthenticationResponse;
import com.alexsitiy.ideas.project.security.RegisterRequest;
import com.alexsitiy.ideas.project.security.SecurityUser;
import com.alexsitiy.ideas.project.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthService {

    private final String JWT_TYPE = "Bearer ";
    @Value("${app.user.default-avatar}")
    private final String USER_DEFAULT_AVATAR;

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtUtil;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .avatar(USER_DEFAULT_AVATAR)
                .build();

        User savedUser = userRepository.save(user);
        log.debug("User {} was created", savedUser);

        String token = jwtUtil.generateToken(SecurityUser.of(savedUser));

        return AuthenticationResponse.builder()
                .token(token)
                .type(JWT_TYPE)
                .build();
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        log.debug("User {} was signed ip", authentication.getPrincipal());

        String token = jwtUtil.generateToken((SecurityUser) authentication.getPrincipal());

        return AuthenticationResponse.builder()
                .token(token)
                .type(JWT_TYPE)
                .build();
    }
}








