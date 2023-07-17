package com.alexsitiy.ideas.project.dto;

import com.alexsitiy.ideas.project.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProjectReadDto {

    private Integer id;
    private String title;
    private String description;
    private String image;
    private String doc;
    private Status status;
    private Integer likes;
    private Integer dislikes;
    private Instant createdAt;
    private UserReadDto creator;
}
