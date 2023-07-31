package com.alexsitiy.ideas.project.integration.service;

import com.alexsitiy.ideas.project.entity.Comment;
import com.alexsitiy.ideas.project.entity.CommentType;
import com.alexsitiy.ideas.project.integration.IntegrationTestBase;
import com.alexsitiy.ideas.project.repository.CommentRepository;
import com.alexsitiy.ideas.project.service.CommentService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class CommentServiceIT extends IntegrationTestBase {

    private static final Integer PROJECT_1_ID = 1;
    private static final Integer USER_1_ID = 1;
    private static final Integer USER_3_ID = 3;

    private final CommentService commentService;

    private final CommentRepository commentRepository;

    private final EntityManager entityManager;

    @Test
    void create() {
        CommentType type = CommentType.LIKE;
        commentService.create(PROJECT_1_ID, USER_3_ID, type);
        entityManager.clear();

        Optional<Comment> actual = commentRepository.findCommentByProjectIdAndUserId(PROJECT_1_ID, USER_3_ID);
        assertThat(actual).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("type", type);
    }

    @Test
    void update() {
        Comment comment = commentRepository.findCommentByProjectIdAndUserId(PROJECT_1_ID, USER_1_ID).orElseThrow();
        entityManager.clear();

        commentService.update(comment, CommentType.DISLIKE);
        entityManager.flush();
        entityManager.clear();

        Comment actual = entityManager.merge(comment);
        assertThat(actual).isNotNull()
                .hasFieldOrPropertyWithValue("type", CommentType.DISLIKE);
    }

    @Test
    void delete() {
        Comment comment = commentRepository.findCommentByProjectIdAndUserId(PROJECT_1_ID, USER_1_ID).orElseThrow();
        entityManager.clear();

        commentService.delete(comment);

        Optional<Comment> actual = commentRepository.findCommentByProjectIdAndUserId(PROJECT_1_ID, USER_1_ID);
        assertThat(actual).isEmpty();
    }
}