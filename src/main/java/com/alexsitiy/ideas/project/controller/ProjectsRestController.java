package com.alexsitiy.ideas.project.controller;

import com.alexsitiy.ideas.project.dto.ProjectUpdateDto;
import com.alexsitiy.ideas.project.dto.error.FileErrorResponse;
import com.alexsitiy.ideas.project.dto.ProjectCreateDto;
import com.alexsitiy.ideas.project.dto.ProjectReadDto;
import com.alexsitiy.ideas.project.exception.UploadingFileException;
import com.alexsitiy.ideas.project.security.SecurityUser;
import com.alexsitiy.ideas.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

        return projectService.create(projectCreateDto, user.getId())
                .map(ResponseEntity.status(HttpStatus.CREATED)::body)
                .orElseGet(ResponseEntity.badRequest()::build);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@securityService.verifyUserForProject(#id,authentication)")
    public ResponseEntity<ProjectReadDto> update(@PathVariable("id") Integer id,
                                                 @Validated @RequestBody ProjectUpdateDto projectUpdateDto) {
        // TODO: 06.07.2023
        //  1) Check project's creator
        //  2) Update project
        //  3) return updated project
        return projectService.update(id, projectUpdateDto)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @ExceptionHandler({UploadingFileException.class})
    public ResponseEntity<FileErrorResponse> handleUploadingFileException(UploadingFileException fileException) {
        return ResponseEntity.internalServerError().body(FileErrorResponse.of(fileException));
    }
}
