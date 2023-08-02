package com.alexsitiy.ideas.project.dto;

import lombok.Value;

@Value
public class NotificationReadDto {
    Integer id;
    String message;
    boolean stale;
    Integer projectId;
    Integer callerId;
    Integer userId;
}
