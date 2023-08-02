package com.alexsitiy.ideas.project.mapper;

import com.alexsitiy.ideas.project.dto.NotificationReadDto;
import com.alexsitiy.ideas.project.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationReadMapper implements Mapper<Notification, NotificationReadDto> {

    @Override
    public NotificationReadDto map(Notification object) {
        return new NotificationReadDto(
                object.getId(),
                object.getMessage(),
                object.isStale(),
                object.getProject().getId(),
                object.getCaller().getId(),
                object.getUser().getId()
        );
    }
}
