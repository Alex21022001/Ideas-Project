package com.alexsitiy.ideas.project.dto;

import com.alexsitiy.ideas.project.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserFullReadDto {

    private Integer id;
    private String firstname;
    private String lastname;
    private String username;
    private Role role;
}
