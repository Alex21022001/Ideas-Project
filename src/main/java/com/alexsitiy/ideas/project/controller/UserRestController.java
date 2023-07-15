package com.alexsitiy.ideas.project.controller;

import com.alexsitiy.ideas.project.dto.UserFullReadDto;
import com.alexsitiy.ideas.project.dto.UserReadDto;
import com.alexsitiy.ideas.project.dto.UserUpdateDto;
import com.alexsitiy.ideas.project.security.SecurityUser;
import com.alexsitiy.ideas.project.service.UserService;
import com.alexsitiy.ideas.project.validation.ContentType;
import com.alexsitiy.ideas.project.validation.FileCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserRestController {

    private final UserService userService;

    @GetMapping("/current")
    public ResponseEntity<UserFullReadDto> getAuthUser(@AuthenticationPrincipal SecurityUser user) {
        return userService.findById(user.getId())
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.badRequest()::build);
    }

    @GetMapping("/{id}/avatar")
    public ResponseEntity<byte[]> getAvatar(@PathVariable("id") Integer userId) {
        return userService.getAvatar(userId)
                .map(bytes -> ResponseEntity
                        .status(200)
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .contentLength(bytes.length)
                        .body(bytes))
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @PutMapping
    public ResponseEntity<UserFullReadDto> update(@RequestBody @Validated UserUpdateDto updateDto,
                                                  @AuthenticationPrincipal SecurityUser user) {
        return userService.update(user.getId(), updateDto)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @PutMapping("/avatar")
    public ResponseEntity<?> updateAvatar(@RequestParam("avatar")
                                          @FileCheck(nullable = false, contentType = {
                                                  ContentType.IMAGE_PNG_VALUE, ContentType.IMAGE_JPEG_VALUE})
                                          MultipartFile image,
                                          @AuthenticationPrincipal SecurityUser user) {

        userService.updateAvatar(user.getId(), image);
        return ResponseEntity.noContent().build();
    }

}
