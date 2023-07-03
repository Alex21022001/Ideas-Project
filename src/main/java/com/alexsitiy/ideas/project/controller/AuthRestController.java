package com.alexsitiy.ideas.project.controller;

import com.alexsitiy.ideas.project.security.RegisterRequest;
import com.alexsitiy.ideas.project.security.AuthenticationResponse;
import com.alexsitiy.ideas.project.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Validated RegisterRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(request));
    }
}
