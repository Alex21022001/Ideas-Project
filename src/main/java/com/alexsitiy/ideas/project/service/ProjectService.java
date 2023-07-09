package com.alexsitiy.ideas.project.service;

import com.alexsitiy.ideas.project.dto.ProjectCreateDto;
import com.alexsitiy.ideas.project.dto.ProjectFilter;
import com.alexsitiy.ideas.project.dto.ProjectReadDto;
import com.alexsitiy.ideas.project.dto.ProjectUpdateDto;
import com.alexsitiy.ideas.project.entity.*;
import com.alexsitiy.ideas.project.mapper.ProjectCreateMapper;
import com.alexsitiy.ideas.project.mapper.ProjectReadMapper;
import com.alexsitiy.ideas.project.repository.CommentRepository;
import com.alexsitiy.ideas.project.repository.ProjectRepository;
import com.alexsitiy.ideas.project.repository.UserRepository;
import com.alexsitiy.ideas.project.util.QPredicate;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ProjectService {

    private final S3Service s3Service;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    private final ProjectCreateMapper projectCreateMapper;
    private final ProjectReadMapper projectReadMapper;


    public Page<ProjectReadDto> findAll(ProjectFilter filter, Pageable pageable) {
        Predicate predicate = QPredicate.builder()
                .add(filter.title(), QProject.project.title::containsIgnoreCase)
                .add(filter.statuses(), QProject.project.status::in)
                .buildAll();

        return projectRepository.findAll(predicate, pageable)
                .map(projectReadMapper::map);
    }

    public Optional<ProjectReadDto> findById(Integer id) {
        return projectRepository.findByIdWithUser(id)
                .map(projectReadMapper::map);
    }

    @Transactional
    public Optional<ProjectReadDto> create(ProjectCreateDto projectDto, Integer userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    Project project = projectCreateMapper.map(projectDto);

                    uploadFile(projectDto.getImage())
                            .ifPresent(project::setImagePath);

                    uploadFile(projectDto.getDocs())
                            .ifPresent(project::setDocsPath);

                    user.addProject(project);
                    projectRepository.save(project);
                    log.debug("Project {} was created", project);
                    return project;
                })
                .map(projectReadMapper::map);
    }

    @Transactional
    public Optional<ProjectReadDto> update(Integer id, ProjectUpdateDto projectDto) {
        return projectRepository.findByIdWithUser(id)
                .map(project -> {
                    project.setDescription(projectDto.getDescription());
                    project.setTitle(projectDto.getTitle());
                    return project;
                })
                .map(projectRepository::saveAndFlush)
                .map(projectReadMapper::map);
    }

    @Transactional
    public void updateImage(Integer id, MultipartFile file) {
        projectRepository.findById(id)
                .ifPresent(project -> uploadFile(file)
                        .ifPresent(imagePath -> {
                            project.setImagePath(imagePath);
                            projectRepository.saveAndFlush(project);
                            log.debug("Project: {} was updated. Image was chanced to {}", project, imagePath);
                        }));
    }

    @Transactional
    public void updateDoc(Integer id, MultipartFile file) {
        projectRepository.findById(id)
                .ifPresent(project -> uploadFile(file)
                        .ifPresent(docPath -> {
                            project.setDocsPath(docPath);
                            projectRepository.saveAndFlush(project);
                            log.debug("Project:{} was updated. Doc was chanced to {}", project, file.getOriginalFilename());
                        }));
    }

    @Transactional
    public boolean delete(Integer id) {
        return projectRepository.findById(id)
                .map(project -> {
                    projectRepository.delete(project);
                    projectRepository.flush();
                    log.debug("Product:{} was deleted", project);
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public boolean likeProject(Integer id, Integer userId) {
        return comment(id, userId, CommentType.LIKE);
    }

    @Transactional
    public boolean dislikeProject(Integer id, Integer userId) {
        return comment(id, userId, CommentType.DISLIKE);
    }

    private boolean comment(Integer projectId, Integer userId, CommentType commentType) {
        return projectRepository.findById(projectId)
                .map(project -> {
                    commentRepository.findCommentByProjectIdAndUserId(projectId, userId)
                            .ifPresentOrElse(comment -> {
                                if (comment.getType().equals(commentType)) {
                                    commentRepository.delete(comment);
                                    commentRepository.flush();
                                    log.debug("Comment: {} was deleted", comment);
                                } else {
                                    comment.setType(commentType);
                                    commentRepository.saveAndFlush(comment);
                                    log.debug("Comment: {} was updated. CommentType was changed to {}", comment, commentType);
                                }

                            }, () -> {
                                commentRepository.save(Comment.of(project,
                                        userRepository.getReferenceById(userId),
                                        commentType));
                                log.debug("User with ID: {} {}ED Project: {}", userId, commentType, project);
                            });
                    return true;
                })
                .orElse(false);
    }

    private Optional<String> uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty())
            return Optional.empty();
        else {
            return s3Service.upload(file, Project.class);
        }
    }
}














