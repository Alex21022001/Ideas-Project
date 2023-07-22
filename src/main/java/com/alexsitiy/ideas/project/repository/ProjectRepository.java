package com.alexsitiy.ideas.project.repository;

import com.alexsitiy.ideas.project.entity.CommentType;
import com.alexsitiy.ideas.project.entity.Project;
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
    Optional<Project> findById(Integer integer);

    @Override
    @EntityGraph(attributePaths = {"user", "reaction", "status"})
    Page<Project> findAll(Predicate predicate, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "reaction", "status"})
    Page<Project> findAllByUserId(Integer userId, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "reaction", "status"})
    @Query("SELECT p FROM Project p JOIN Comment c on p.id = c.project.id WHERE c.user.id = :userId AND c.type = :type")
    Page<Project> findAllCommentedByUserIdAndCommentType(Integer userId, CommentType type, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "reaction", "status"})
    @Query("SELECT p FROM Project p WHERE p.id = :id")
    Optional<Project> findByIdWithUserAndReactionAndStatus(Integer id);

}
