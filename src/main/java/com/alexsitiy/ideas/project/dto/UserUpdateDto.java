package com.alexsitiy.ideas.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class UserUpdateDto {

    @NotBlank
    @Size(min = 2)
    String firstname;

    @NotBlank
    @Size(min = 2)
    String lastname;
}
