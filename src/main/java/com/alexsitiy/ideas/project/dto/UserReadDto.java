package com.alexsitiy.ideas.project.dto;

import com.alexsitiy.ideas.project.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserReadDto {
    private Integer id;
    private String firstname;
    private String lastname;
    private Role role;
    private String username;
}
