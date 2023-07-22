package com.alexsitiy.ideas.project.controller;

import com.alexsitiy.ideas.project.dto.*;
import com.alexsitiy.ideas.project.exception.NoSuchProjectException;
import com.alexsitiy.ideas.project.security.SecurityUser;
import com.alexsitiy.ideas.project.service.ProjectService;
import com.alexsitiy.ideas.project.validation.ContentType;
import com.alexsitiy.ideas.project.validation.FileCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
                                                                ProjectSort projectSort) {

        Page<ProjectReadDto> projectReadPage = projectService.findAll(filter, projectSort.getPageable());

        return ResponseEntity.ok(PageResponse.of(projectReadPage));
    }

    @GetMapping("/user")
    public ResponseEntity<PageResponse<ProjectReadDto>> findAllByUser(@AuthenticationPrincipal SecurityUser user,
                                                                      ProjectSort sort) {

        Page<ProjectReadDto> projects = projectService.findAllByUserId(user.getId(), sort.getPageable());

        return ResponseEntity.ok(PageResponse.of(projects));
    }

    @GetMapping("/user/liked")
    public ResponseEntity<PageResponse<ProjectReadDto>> findAllLikedProjectsByUser(@AuthenticationPrincipal SecurityUser user,
                                                                                   ProjectSort sort) {

        Page<ProjectReadDto> projects = projectService.findAllLikedByUserId(user.getId(), sort.getPageable());

        return ResponseEntity.ok(PageResponse.of(projects));
    }

    @GetMapping("/user/disliked")
    public ResponseEntity<PageResponse<ProjectReadDto>> findAllDislikedProjectsByUser(@AuthenticationPrincipal SecurityUser user,
                                                                                      ProjectSort sort) {

        Page<ProjectReadDto> projects = projectService.findAllDislikedByUserId(user.getId(), sort.getPageable());

        return ResponseEntity.ok(PageResponse.of(projects));
    }

    @GetMapping("/user/history")
    public ResponseEntity<PageResponse<ProjectHistoryDto>> findAllProjectHistoryByUser(@AuthenticationPrincipal
                                                                                       SecurityUser user,
                                                                                       Pageable pageable) {

        Page<ProjectHistoryDto> projectHistory =
                projectService.findAllProjectHistoryByUser(user.getUsername(), pageable);

        return ResponseEntity.ok(PageResponse.of(projectHistory));
    }

    @PostMapping
    public ResponseEntity<ProjectReadDto> create(@Validated ProjectCreateDto projectCreateDto,
                                                 @AuthenticationPrincipal SecurityUser user) {

        return projectService.create(projectCreateDto, user.getId())
                .map(ResponseEntity.status(HttpStatus.CREATED)::body)
                .orElseGet(ResponseEntity.badRequest()::build);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Integer id) {
        return projectService.downloadImage(id)
                .map(bytes -> ResponseEntity
                        .status(200)
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .contentLength(bytes.length)
                        .body(bytes))
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @GetMapping("/{id}/doc")
    public ResponseEntity<byte[]> getDoc(@PathVariable Integer id) {
        return projectService.downloadDoc(id)
                .map(bytes -> ResponseEntity
                        .status(200)
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .contentLength(bytes.length)
                        .body(bytes))
                .orElseGet(ResponseEntity.notFound()::build);
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
                                         @FileCheck(nullable = false, contentType = {
                                                 ContentType.IMAGE_PNG_VALUE, ContentType.IMAGE_JPEG_VALUE})
                                         MultipartFile file) {

        projectService.updateImage(id, file);
        return ResponseEntity.status(HttpStatus.RESET_CONTENT).build();
    }

    @PutMapping("/{id}/doc")
    @PreAuthorize("@securityService.verifyUserForProject(#id,authentication)")
    public ResponseEntity<?> updateDoc(@PathVariable("id") Integer id,
                                       @RequestParam("doc")
                                       @FileCheck(nullable = false, contentType = {ContentType.APPLICATION_PDF_VALUE})
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
        projectService.likeProject(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/dislike")
    public ResponseEntity<?> dislikeProject(@PathVariable Integer id,
                                            @AuthenticationPrincipal SecurityUser user) {
        projectService.dislikeProject(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<?> acceptProject(@PathVariable Integer id) {
        projectService.acceptProject(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<?> rejectProject(@PathVariable Integer id) {
        projectService.rejectProject(id);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NoSuchProjectException.class)
    public ResponseEntity<?> handleNoSuchProjectException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<?> handleObjectOptimisticLockingFailureException() {
        String errorMessage = "The Project's status has been modified by someone else. Please refresh and try again.";
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }

}
