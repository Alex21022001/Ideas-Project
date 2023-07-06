package com.alexsitiy.ideas.project.controller;

import com.alexsitiy.ideas.project.dto.error.FileErrorResponse;
import com.alexsitiy.ideas.project.dto.ProjectCreateDto;
import com.alexsitiy.ideas.project.dto.ProjectReadDto;
import com.alexsitiy.ideas.project.exception.UploadingFileException;
import com.alexsitiy.ideas.project.security.SecurityUser;
import com.alexsitiy.ideas.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectsRestController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectReadDto> create(@Validated ProjectCreateDto projectCreateDto,
                                                 @AuthenticationPrincipal SecurityUser user) {
        // TODO: 04.07.2023
        //  + Validate project
        //  2) Upload files to S3
        //  3) Add project to DB
        //  4) return new ProjectReadDto
        Optional<ProjectReadDto> createdProject = projectService.create(projectCreateDto, user.getId());
        // TODO: 05.07.2023 Handle UploadingEx
        // TODO: 05.07.2023 Change ErrorResponse if file is invalid
        return createdProject.map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.badRequest()::build);
    }

    @ExceptionHandler({UploadingFileException.class})
    public ResponseEntity<FileErrorResponse> handleUploadingFileException(UploadingFileException fileException){
        return ResponseEntity.internalServerError().body(FileErrorResponse.of(fileException));
    }
}
