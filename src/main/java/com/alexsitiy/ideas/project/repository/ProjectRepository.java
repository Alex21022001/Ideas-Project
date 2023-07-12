package com.alexsitiy.ideas.project.repository;

import com.alexsitiy.ideas.project.entity.Project;
import com.querydsl.core.types.Predicate;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Integer>,
        QuerydslPredicateExecutor<Project>{

    Optional<Project> findByTitle(String title);

    @Query("SELECT p FROM Project p WHERE p.id =:id")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Project> findByIdWithLock(Integer id);

    @Override
    @EntityGraph(attributePaths = {"user"})
    Page<Project> findAll(Predicate predicate, Pageable pageable);

    @EntityGraph(attributePaths = "user")
    @Query("SELECT p FROM Project p WHERE p.id = :id")
    Optional<Project> findByIdWithUser(Integer id);

}
