package com.alexsitiy.ideas.project.mapper;

import com.alexsitiy.ideas.project.dto.ProjectReadDto;
import com.alexsitiy.ideas.project.entity.Project;
import com.alexsitiy.ideas.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectReadMapper implements Mapper<Project, ProjectReadDto> {

    private final UserReadMapper userReadMapper;

    @Override
    public ProjectReadDto map(Project object) {
        return ProjectReadDto.builder()
                .id(object.getId())
                .title(object.getTitle())
                .description(object.getDescription())
                .image(object.getImagePath())
                .docs(object.getDocsPath())
                .status(object.getStatus())
                .creator(userReadMapper.map(object.getUser()))
                .build();
    }
}