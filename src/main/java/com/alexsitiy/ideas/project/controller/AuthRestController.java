package com.alexsitiy.ideas.project.controller;

import com.alexsitiy.ideas.project.dto.ErrorMessage;
import com.alexsitiy.ideas.project.security.AuthenticationRequest;
import com.alexsitiy.ideas.project.security.RegisterRequest;
import com.alexsitiy.ideas.project.security.AuthenticationResponse;
import com.alexsitiy.ideas.project.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Validated AuthenticationRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException badCredentialsException) {
        return ResponseEntity.badRequest().body(new ErrorMessage(badCredentialsException.getLocalizedMessage()));
    }
}








