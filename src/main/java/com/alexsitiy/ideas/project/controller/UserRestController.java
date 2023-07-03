package com.alexsitiy.ideas.project.controller;

import com.alexsitiy.ideas.project.dto.UserReadDto;
import com.alexsitiy.ideas.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping("/current")
    public ResponseEntity<UserReadDto> getAuthUser(@AuthenticationPrincipal UserDetails userDetails){
        return userService.findByUsername(userDetails.getUsername())
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.badRequest()::build);
    }
}
