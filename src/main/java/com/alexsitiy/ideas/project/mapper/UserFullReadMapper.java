package com.alexsitiy.ideas.project.mapper;

import com.alexsitiy.ideas.project.dto.UserFullReadDto;
import com.alexsitiy.ideas.project.entity.Project;
import com.alexsitiy.ideas.project.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserFullReadMapper implements Mapper<User, UserFullReadDto>{

    @Override
    public UserFullReadDto map(User object) {
        return UserFullReadDto.builder()
                .id(object.getId())
                .firstname(object.getFirstname())
                .lastname(object.getLastname())
                .username(object.getUsername())
                .role(object.getRole())
                .build();
    }
}
