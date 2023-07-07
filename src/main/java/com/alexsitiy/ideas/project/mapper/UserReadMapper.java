package com.alexsitiy.ideas.project.mapper;

import com.alexsitiy.ideas.project.dto.UserReadDto;
import com.alexsitiy.ideas.project.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserReadMapper implements Mapper<User, UserReadDto> {


    @Override
    public UserReadDto map(User object) {
        return UserReadDto.builder()
                .id(object.getId())
                .firstname(object.getFirstname())
                .lastname(object.getLastname())
                .build();
    }
}
