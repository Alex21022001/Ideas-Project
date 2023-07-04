package com.alexsitiy.ideas.project.dto;

import com.alexsitiy.ideas.project.validation.FileCheck;
import com.alexsitiy.ideas.project.validation.TitleCheck;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectCreateDto {

    @NotBlank
    @TitleCheck
    private String title;

    @NotBlank
    @Size(max = 256)
    private String description;

    @FileCheck(contentType = {"image/png","image/jpg"},nullable = false)
    private MultipartFile image;

    @FileCheck(contentType = {"application/pdf"})
    private MultipartFile docs;

}
