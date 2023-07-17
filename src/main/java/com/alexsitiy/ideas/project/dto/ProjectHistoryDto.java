package com.alexsitiy.ideas.project.dto;

import com.alexsitiy.ideas.project.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.RevisionType;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProjectHistoryDto {

    private Integer id;
    private String title;
    private String description;
    private String image;
    private String doc;
    private Status status;
    private ActionType actionType;
    private LocalDate executedAt;
}
