package com.alexsitiy.ideas.project.dto;

import com.alexsitiy.ideas.project.entity.Project;
import com.alexsitiy.ideas.project.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Value;

@Value
public class NotificationReadDto {
    Integer id;
    String message;
    Integer projectId;
    Integer callerId;
    Integer userId;
}
