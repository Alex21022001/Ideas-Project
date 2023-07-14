package com.alexsitiy.ideas.project.controller;

import com.alexsitiy.ideas.project.dto.*;
import com.alexsitiy.ideas.project.dto.error.FileErrorResponse;
import com.alexsitiy.ideas.project.entity.Project;
import com.alexsitiy.ideas.project.exception.NoSuchProjectException;
import com.alexsitiy.ideas.project.exception.UploadingFileException;
import com.alexsitiy.ideas.project.security.SecurityUser;
import com.alexsitiy.ideas.project.service.ProjectService;
import com.alexsitiy.ideas.project.validation.FileCheck;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Validated
public class ProjectsRestController {

    private final ProjectService projectService;

    @GetMapping("/{id}")
    public ResponseEntity<ProjectReadDto> findById(@PathVariable Integer id) {
        return projectService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProjectReadDto>> findAll(ProjectFilter filter,
                                                                SortRequest sortRequest) {

        Page<ProjectReadDto> projectReadPage = projectService.findAll(filter, sortRequest.getPageable());

        return ResponseEntity.ok(PageResponse.of(projectReadPage));
    }

    @GetMapping("/user")
    public ResponseEntity<PageResponse<ProjectReadDto>> findAllByUser(@AuthenticationPrincipal SecurityUser user,
                                                                      SortRequest sort) {

        Page<ProjectReadDto> projects = projectService.findAllByUserId(user.getId(), sort.getPageable());

        return ResponseEntity.ok(PageResponse.of(projects));
    }

    @GetMapping("/user/liked")
    public ResponseEntity<PageResponse<ProjectReadDto>> findAllLikedProjectsByUser(@AuthenticationPrincipal SecurityUser user,
                                                                                   SortRequest sort) {

        Page<ProjectReadDto> projects = projectService.findAllLikedByUserId(user.getId(), sort.getPageable());

        return ResponseEntity.ok(PageResponse.of(projects));
    }

    @GetMapping("/user/disliked")
    public ResponseEntity<PageResponse<ProjectReadDto>> findAllDislikedProjectsByUser(@AuthenticationPrincipal SecurityUser user,
                                                                                      SortRequest sort) {

        Page<ProjectReadDto> projects = projectService.findAllDislikedByUserId(user.getId(), sort.getPageable());

        return ResponseEntity.ok(PageResponse.of(projects));
    }

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

        return projectService.update(id, projectUpdateDto)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @PutMapping("/{id}/image")
    @PreAuthorize("@securityService.verifyUserForProject(#id,authentication)")
    public ResponseEntity<?> updateImage(@PathVariable("id") Integer id,
                                         @RequestParam("image")
                                         @FileCheck(nullable = false, contentType = {"image/png", "image/jpg"})
                                         MultipartFile file) {

        projectService.updateImage(id, file);
        return ResponseEntity.status(HttpStatus.RESET_CONTENT).build();
    }

    @PutMapping("/{id}/doc")
    @PreAuthorize("@securityService.verifyUserForProject(#id,authentication)")
    public ResponseEntity<?> updateDoc(@PathVariable("id") Integer id,
                                       @RequestParam("doc")
                                       @FileCheck(nullable = false, contentType = {"application/pdf"})
                                       MultipartFile file) {

        projectService.updateDoc(id, file);
        return ResponseEntity.status(HttpStatus.RESET_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@securityService.verifyUserForProject(#id,authentication)")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {

        return projectService.delete(id) ?
                ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> likeProject(@PathVariable Integer id,
                                         @AuthenticationPrincipal SecurityUser user) {

        return projectService.likeProject(id, user.getId()) ?
                ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/dislike")
    public ResponseEntity<?> dislikeProject(@PathVariable Integer id,
                                            @AuthenticationPrincipal SecurityUser user) {

        return projectService.dislikeProject(id, user.getId()) ?
                ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @ExceptionHandler(NoSuchProjectException.class)
    public ResponseEntity<?> handleNoSuchProjectException() {
        return ResponseEntity.notFound().build();
    }

}
