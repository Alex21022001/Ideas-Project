package com.alexsitiy.ideas.project.mapper;

import com.alexsitiy.ideas.project.dto.ActionType;
import com.alexsitiy.ideas.project.dto.ProjectHistoryDto;
import com.alexsitiy.ideas.project.entity.HistoryEntity;
import com.alexsitiy.ideas.project.entity.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectHistoryMapper implements Mapper<HistoryEntity<Project>, ProjectHistoryDto>{

    @Override
    public ProjectHistoryDto map(HistoryEntity<Project> object) {
        Project project = object.getEntity();
        return ProjectHistoryDto.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .image(project.getImagePath())
                .doc(project.getDocPath())
                .status(project.getStatus())
                .actionType(ActionType.getByRevisionType(object.getRevisionType()))
                .executedAt(object.getDateTime())
                .build();
    }
}
