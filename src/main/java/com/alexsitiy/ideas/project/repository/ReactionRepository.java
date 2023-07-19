package com.alexsitiy.ideas.project.repository;

import com.alexsitiy.ideas.project.entity.ProjectReaction;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReactionRepository extends JpaRepository<ProjectReaction, Integer> {


    Optional<ProjectReaction> findReactionByProjectId(Integer projectId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM ProjectReaction r WHERE r.project.id = :projectId")
    Optional<ProjectReaction> findByIdWithLock(Integer projectId);
}
