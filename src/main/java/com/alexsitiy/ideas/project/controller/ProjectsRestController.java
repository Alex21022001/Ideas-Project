package com.alexsitiy.ideas.project.controller;

import com.alexsitiy.ideas.project.dto.*;
import com.alexsitiy.ideas.project.dto.sort.ProjectSort;
import com.alexsitiy.ideas.project.entity.Status;
import com.alexsitiy.ideas.project.exception.NoSuchProjectException;
import com.alexsitiy.ideas.project.security.SecurityUser;
import com.alexsitiy.ideas.project.service.ProjectService;
import com.alexsitiy.ideas.project.validation.ContentType;
import com.alexsitiy.ideas.project.validation.FileCheck;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Get a Project by given ID")
    public ResponseEntity<ProjectReadDto> findById(@PathVariable Integer id) {
        return projectService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @GetMapping
    @Operation(summary = "Get all Projects with sort and pagination")
    public ResponseEntity<PageResponse<ProjectReadDto>> findAll(ProjectFilter filter,
                                                                ProjectSort projectSort) {

        Page<ProjectReadDto> projectReadPage = projectService.findAll(filter, projectSort.getPageable());

        return ResponseEntity.ok(PageResponse.of(projectReadPage));
    }

    @GetMapping("/user")
    @Operation(summary = "Get all Projects created by an authenticated User with sort and pagination")
    public ResponseEntity<PageResponse<ProjectReadDto>> findAllByUser(@AuthenticationPrincipal SecurityUser user,
                                                                      ProjectSort sort) {

        Page<ProjectReadDto> projects = projectService.findAllByUserId(user.getId(), sort.getPageable());

        return ResponseEntity.ok(PageResponse.of(projects));
    }

    @GetMapping("/user/liked")
    @Operation(summary = "Get all Projects liked by an authenticated User with sort and pagination")
    public ResponseEntity<PageResponse<ProjectReadDto>> findAllLikedProjectsByUser(@AuthenticationPrincipal SecurityUser user,
                                                                                   ProjectSort sort) {

        Page<ProjectReadDto> projects = projectService.findAllLikedByUserId(user.getId(), sort.getPageable());

        return ResponseEntity.ok(PageResponse.of(projects));
    }

    @GetMapping("/user/disliked")
    @Operation(summary = "Get all Projects disliked by an authenticated User with sort and pagination")
    public ResponseEntity<PageResponse<ProjectReadDto>> findAllDislikedProjectsByUser(@AuthenticationPrincipal SecurityUser user,
                                                                                      ProjectSort sort) {

        Page<ProjectReadDto> projects = projectService.findAllDislikedByUserId(user.getId(), sort.getPageable());

        return ResponseEntity.ok(PageResponse.of(projects));
    }

    @GetMapping("/user/history")
    @Operation(summary = "Get all ProjectHistory for an authenticated User")
    public ResponseEntity<PageResponse<ProjectHistoryDto>> findAllProjectHistoryByUser(@AuthenticationPrincipal
                                                                                       SecurityUser user,
                                                                                       Pageable pageable) {

        Page<ProjectHistoryDto> projectHistory =
                projectService.findAllProjectHistoryByUser(user.getUsername(), pageable);

        return ResponseEntity.ok(PageResponse.of(projectHistory));
    }

    @GetMapping("/expert/accepted")
    @Operation(summary = "Get all Projects accepted by an authenticated Expert")
    public ResponseEntity<PageResponse<ProjectReadDto>> findAllAcceptedByExpert(@AuthenticationPrincipal
                                                                                SecurityUser user,
                                                                                ProjectSort sort) {
        Page<ProjectReadDto> projects = projectService
                .findAllEstimatedByExpertIdAndStatus(user.getId(), Status.ACCEPTED, sort.getPageable());
        return ResponseEntity.ok(PageResponse.of(projects));
    }

    @GetMapping("/expert/rejected")
    @Operation(summary = "Get all Projects rejected by an authenticated Expert")
    public ResponseEntity<PageResponse<ProjectReadDto>> findAllRejectedByExpert(@AuthenticationPrincipal
                                                                                SecurityUser user,
                                                                                ProjectSort sort) {
        Page<ProjectReadDto> projects = projectService
                .findAllEstimatedByExpertIdAndStatus(user.getId(), Status.REJECTED, sort.getPageable());
        return ResponseEntity.ok(PageResponse.of(projects));
    }

    @PostMapping
    @Operation(summary = "Create Project")
    public ResponseEntity<ProjectReadDto> create(@Validated ProjectCreateDto projectCreateDto,
                                                 @AuthenticationPrincipal SecurityUser user) {

        return projectService.create(projectCreateDto, user.getId())
                .map(ResponseEntity.status(HttpStatus.CREATED)::body)
                .orElseGet(ResponseEntity.badRequest()::build);
    }

    @GetMapping("/{id}/image")
    @Operation(summary = "Get Project's image by Project ID")
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
    @Operation(summary = "Get Project's doc by Project ID")
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
    @Operation(summary = "Update a Project by Project ID")
    public ResponseEntity<ProjectReadDto> update(@PathVariable("id") Integer id,
                                                 @Validated @RequestBody ProjectUpdateDto projectUpdateDto) {

        return projectService.update(id, projectUpdateDto)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @PutMapping("/{id}/image")
    @PreAuthorize("@securityService.verifyUserForProject(#id,authentication)")
    @Operation(summary = "Update Project's image by Project ID")
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
    @Operation(summary = "Update Project's doc by Project ID")
    public ResponseEntity<?> updateDoc(@PathVariable("id") Integer id,
                                       @RequestParam("doc")
                                       @FileCheck(nullable = false, contentType = {ContentType.APPLICATION_PDF_VALUE})
                                       MultipartFile file) {

        projectService.updateDoc(id, file);
        return ResponseEntity.status(HttpStatus.RESET_CONTENT).build();
    }

    @PostMapping("/{id}/like")
    @Operation(summary = "Like a Project by Project ID")
    public ResponseEntity<?> likeProject(@PathVariable Integer id,
                                         @AuthenticationPrincipal SecurityUser user) {
        projectService.likeProject(id, user.getId());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/dislike")
    @Operation(summary = "Dislike a Project by Project ID")
    public ResponseEntity<?> dislikeProject(@PathVariable Integer id,
                                            @AuthenticationPrincipal SecurityUser user) {
        projectService.dislikeProject(id, user.getId());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/accept")
    @Operation(summary = "Accept a Project by Project ID")
    public ResponseEntity<?> acceptProject(@PathVariable Integer id,
                                           @AuthenticationPrincipal SecurityUser user) {
        projectService.acceptProject(id, user.getId());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Reject a Project by Project ID")
    public ResponseEntity<?> rejectProject(@PathVariable Integer id,
                                           @AuthenticationPrincipal SecurityUser user) {
        projectService.rejectProject(id, user.getId());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@securityService.verifyUserForProject(#id,authentication)")
    @Operation(summary = "Delete a Project by Project ID")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {

        return projectService.delete(id) ?
                ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
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
