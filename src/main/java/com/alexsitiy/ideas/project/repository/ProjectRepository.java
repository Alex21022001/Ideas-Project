package com.alexsitiy.ideas.project.repository;

import com.alexsitiy.ideas.project.entity.CommentType;
import com.alexsitiy.ideas.project.entity.Project;
import com.alexsitiy.ideas.project.entity.Status;
import com.alexsitiy.ideas.project.repository.custom.ProjectHistoryRepository;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Integer>,
        QuerydslPredicateExecutor<Project>,
        ProjectHistoryRepository {

    Optional<Project> findByTitle(String title);

    @Override
    @EntityGraph(attributePaths = {"reaction", "status"})
    Optional<Project> findById(Integer id);

    @Query("SELECT p FROM Project p " +
           "JOIN FETCH p.user " +
           "JOIN FETCH p.reaction " +
           "JOIN FETCH p.status s " +
           "WHERE p.id = :id")
    Optional<Project> findByIdWithUser(Integer id);

    @Override
    @EntityGraph(attributePaths = {"user", "reaction", "status"})
    Page<Project> findAll(Predicate predicate, Pageable pageable);

    @Query("SELECT p FROM Project p " +
           "JOIN FETCH p.user " +
           "JOIN FETCH p.reaction " +
           "JOIN FETCH p.status s " +
           "WHERE p.user.id = :userId")
    Page<Project> findAllByUserId(Integer userId, Pageable pageable);

    @Query("SELECT p FROM Project p " +
           "JOIN FETCH p.user " +
           "JOIN FETCH p.reaction " +
           "JOIN FETCH p.status " +
           "JOIN p.comments c " +
           "WHERE c.user.id = :userId AND c.type = :type")
    Page<Project> findAllCommentedByUserIdAndCommentType(Integer userId, CommentType type, Pageable pageable);

    @Query("SELECT p FROM Project p " +
           "JOIN FETCH p.user " +
           "JOIN FETCH p.reaction " +
           "JOIN FETCH p.status s " +
           "WHERE s.status = :status " +
           "AND s.expert.id = :expertId")
    Page<Project> findAllByStatusExpertIdAndStatusStatus(Integer expertId, Status status, Pageable pageable);
}
