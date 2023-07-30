package com.alexsitiy.ideas.project.service;

import com.alexsitiy.ideas.project.dto.*;
import com.alexsitiy.ideas.project.entity.*;
import com.alexsitiy.ideas.project.exception.NoSuchProjectException;
import com.alexsitiy.ideas.project.mapper.ProjectCreateMapper;
import com.alexsitiy.ideas.project.mapper.ProjectHistoryMapper;
import com.alexsitiy.ideas.project.mapper.ProjectReadMapper;
import com.alexsitiy.ideas.project.repository.*;
import com.alexsitiy.ideas.project.util.QPredicate;
import com.querydsl.core.types.Predicate;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static com.alexsitiy.ideas.project.entity.QProject.project;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ProjectService {

    private final ApplicationEventPublisher eventPublisher;

    private final S3Service s3Service;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ReactionRepository reactionRepository;
    private final ProjectStatusRepository projectStatusRepository;

    private final ProjectCreateMapper projectCreateMapper;
    private final ProjectReadMapper projectReadMapper;
    private final ProjectHistoryMapper projectHistoryMapper;

    private final EntityManager entityManager;

    public Page<ProjectReadDto> findAll(ProjectFilter filter, Pageable pageable) {
        Predicate predicate = QPredicate.builder()
                .add(filter.title(), project.title::containsIgnoreCase)
                .add(filter.status(), project.status.status::in)
                .buildAll();

        return projectRepository.findAll(predicate, pageable)
                .map(projectReadMapper::map);
    }

    public Optional<ProjectReadDto> findById(Integer id) {
        return projectRepository.findByIdWithUser(id)
                .map(projectReadMapper::map);
    }

    public Page<ProjectReadDto> findAllByUserId(Integer userId, Pageable pageable) {
        return projectRepository.findAllByUserId(userId, pageable)
                .map(projectReadMapper::map);
    }

    public Page<ProjectReadDto> findAllLikedByUserId(Integer userId, Pageable pageable) {
        return projectRepository.findAllCommentedByUserIdAndCommentType(userId, CommentType.LIKE, pageable)
                .map(projectReadMapper::map);
    }

    public Page<ProjectReadDto> findAllDislikedByUserId(Integer userId, Pageable pageable) {
        return projectRepository.findAllCommentedByUserIdAndCommentType(userId, CommentType.DISLIKE, pageable)
                .map(projectReadMapper::map);
    }

    public Page<ProjectHistoryDto> findAllProjectHistoryByUser(String username, Pageable pageable) {
        return projectRepository.findAllProjectHistoryByUsername(username, pageable)
                .map(projectHistoryMapper::map);
    }

    public Page<ProjectReadDto> findAllEstimatedByExpertIdAndStatus(Integer id, Status status, Pageable pageable) {
        return projectRepository.findAllByStatusExpertIdAndStatusStatus(id, status, pageable)
                .map(projectReadMapper::map);
    }

    public Optional<byte[]> downloadImage(Integer id) {
        return projectRepository.findById(id)
                .flatMap(project -> s3Service.download(project.getImagePath(), Project.class));
    }

    public Optional<byte[]> downloadDoc(Integer id) {
        return projectRepository.findById(id)
                .filter(project -> project.getDocPath() != null)
                .flatMap(project -> s3Service.download(project.getDocPath(), Project.class));
    }

    @Transactional
    public Optional<ProjectReadDto> create(ProjectCreateDto projectDto, Integer userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    Project project = projectCreateMapper.map(projectDto);

                    uploadFile(projectDto.getImage())
                            .ifPresent(project::setImagePath);

                    uploadFile(projectDto.getDoc())
                            .ifPresent(project::setDocPath);

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
                            project.setDocPath(docPath);
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
    public void likeProject(Integer id, Integer userId) {
        comment(id, userId, CommentType.LIKE);
    }

    @Transactional
    public void dislikeProject(Integer id, Integer userId) {
        comment(id, userId, CommentType.DISLIKE);
    }

    @Transactional
    public void acceptProject(Integer projectId, Integer expertId) {
        changeProjectStatus(projectId, expertId, Status.ACCEPTED);
    }

    @Transactional
    public void rejectProject(Integer projectId, Integer expertId) {
        changeProjectStatus(projectId, expertId, Status.REJECTED);
    }

    private void changeProjectStatus(Integer projectId, Integer expertId, Status status) {
        projectStatusRepository.findByProjectId(projectId)
                .ifPresentOrElse(projectStatus -> {
                            if (projectStatus.getStatus() == Status.IN_PROGRESS) {
                                projectStatus.setStatus(status);
                                projectStatus.setExpert(userRepository.getReferenceById(expertId));
                                projectStatusRepository.saveAndFlush(projectStatus);
                                log.debug("ProjectStatus: {} was updated", projectStatus);
                            } else {
                                throw new AccessDeniedException("Can't change Project.status because of it has already been changed");
                            }
                        },
                        () -> {
                            throw new NoSuchProjectException("There is no such Project with id:" + projectId);
                        });
    }


    private void comment(Integer projectId, Integer userId, CommentType commentType) {
        reactionRepository.findByIdWithLock(projectId)
                .ifPresentOrElse(
                        reaction -> commentRepository.findCommentByProjectIdAndUserId(projectId, userId)
                                .ifPresentOrElse(comment ->
                                                handleExistingComment(commentType, reaction, comment),
                                        () -> {
                                            handleNotExistingComment(projectId, userId, commentType, reaction);
                                        }),
                        () -> {
                            throw new NoSuchProjectException("There is no such Project with id:" + projectId);
                        });

    }

    private void handleExistingComment(CommentType commentType, ProjectReaction projectReaction, Comment comment) {
        if (comment.getType().equals(commentType)) {
            projectReaction.decrement(commentType);
// TODO: 30.07.2023 On Comment deleted
            reactionRepository.save(projectReaction);
            commentRepository.delete(comment);
            commentRepository.flush();
            log.debug("Comment: {} was deleted", comment);
            log.debug("Reaction: {}, {}s were decremented", projectReaction, commentType.name().toLowerCase());
        } else {
            comment.setType(commentType);
            projectReaction.change(commentType);
// TODO: 30.07.2023 On Comment updated
            reactionRepository.save(projectReaction);
            commentRepository.save(comment);
            commentRepository.flush();
            log.debug("Comment: {} was updated. CommentType was changed to {}", comment, commentType);
            log.debug("Reaction: {}, {}s were incremented", projectReaction, commentType.name().toLowerCase());
        }
    }

    private void handleNotExistingComment(Integer projectId, Integer userId, CommentType commentType, ProjectReaction projectReaction) {
        Comment comment = Comment.of(
                projectRepository.getReferenceById(projectId),
                userRepository.getReferenceById(userId),
                commentType);
        projectReaction.increment(commentType);
// TODO: 30.07.2023 On Comment created
        commentRepository.save(comment);
        reactionRepository.saveAndFlush(projectReaction);
        log.debug("User with ID: {} {}ED Project: {}. Current Reactions: {}", userId, commentType, projectId, projectReaction);
    }

    private Optional<String> uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty())
            return Optional.empty();
        else {
            return s3Service.upload(file, Project.class);
        }
    }


}














