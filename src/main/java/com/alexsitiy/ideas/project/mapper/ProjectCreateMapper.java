package com.alexsitiy.ideas.project.mapper;

import com.alexsitiy.ideas.project.dto.ProjectCreateDto;
import com.alexsitiy.ideas.project.entity.Project;
import com.alexsitiy.ideas.project.entity.Status;
import org.springframework.stereotype.Component;

@Component
public class ProjectCreateMapper implements Mapper<ProjectCreateDto, Project>{

    @Override
    public Project map(ProjectCreateDto object) {
        return Project.builder()
                .title(object.getTitle())
                .description(object.getDescription())
                .status(Status.WAITING)
                .likes(0)
                .dislikes(0)
                .build();
    }
}
