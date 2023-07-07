package com.alexsitiy.ideas.project.dto;

import com.alexsitiy.ideas.project.validation.TitleCheck;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectUpdateDto {

    @NotBlank
    @Size(min = 5)
    @TitleCheck
    private String title;

    @NotBlank
    @Size(max = 256)
    private String description;
}
