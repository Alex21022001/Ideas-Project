package com.alexsitiy.ideas.project.dto;

import com.alexsitiy.ideas.project.validation.ContentType;
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
    @Size(min = 5)
    @TitleCheck
    private String title;

    @NotBlank
    @Size(max = 256)
    private String description;

    @FileCheck(contentType = {ContentType.IMAGE_PNG_VALUE, ContentType.IMAGE_JPEG_VALUE}, nullable = false)
    private MultipartFile image;

    @FileCheck(contentType = {ContentType.APPLICATION_PDF_VALUE})
    private MultipartFile doc;

}
