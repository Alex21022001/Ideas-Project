package com.alexsitiy.ideas.project.controller;

import com.alexsitiy.ideas.project.dto.error.ErrorMessage;
import com.alexsitiy.ideas.project.dto.error.ErrorType;
import com.alexsitiy.ideas.project.security.AuthenticationRequest;
import com.alexsitiy.ideas.project.security.RegisterRequest;
import com.alexsitiy.ideas.project.security.AuthenticationResponse;
import com.alexsitiy.ideas.project.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Register a new User with given credentials")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Validated RegisterRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate already existed User by given credentials")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Validated AuthenticationRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException badCredentialsException) {
        return ResponseEntity.badRequest().body(new ErrorMessage(badCredentialsException.getLocalizedMessage(), ErrorType.BAD_CREDENTIALS));
    }
}








