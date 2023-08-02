package com.alexsitiy.ideas.project.controller;

import com.alexsitiy.ideas.project.dto.NotificationReadDto;
import com.alexsitiy.ideas.project.dto.PageResponse;
import com.alexsitiy.ideas.project.dto.sort.NotificationSort;
import com.alexsitiy.ideas.project.security.SecurityUser;
import com.alexsitiy.ideas.project.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationRestController {

    private final NotificationService notificationService;

    @GetMapping("/user")
    public ResponseEntity<PageResponse<NotificationReadDto>> getUserNotifications(@AuthenticationPrincipal SecurityUser user,
                                                                                  NotificationSort sort) {

        Page<NotificationReadDto> notifications = notificationService.findAllByUser(user.getId(), sort.getPageable());
        return ResponseEntity.ok(PageResponse.of(notifications));
    }
}
