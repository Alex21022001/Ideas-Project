package com.alexsitiy.ideas.project.service;

import com.alexsitiy.ideas.project.entity.Comment;
import com.alexsitiy.ideas.project.entity.CommentType;
import com.alexsitiy.ideas.project.repository.CommentRepository;
import com.alexsitiy.ideas.project.repository.ProjectRepository;
import com.alexsitiy.ideas.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public void create(Integer projectId, Integer userId, CommentType commentType) {
        Comment comment = Comment.of(
                projectRepository.getReferenceById(projectId),
                userRepository.getReferenceById(userId),
                commentType);
        commentRepository.save(comment);
        log.debug("User with ID: {} {}ED Project: {}", userId, commentType, projectId);
    }

    @Transactional
    public void update(Comment comment, CommentType commentType){
        comment.setType(commentType);
        comment.setCommentedAt(Instant.now());
        commentRepository.save(comment);
        log.debug("Comment: {} was updated. CommentType was changed to {}", comment, commentType);
    }

    @Transactional
    public void delete(Comment comment){
        commentRepository.delete(comment);
        log.debug("Comment: {} was deleted", comment);
    }
}
