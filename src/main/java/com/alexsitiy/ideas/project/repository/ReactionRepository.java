package com.alexsitiy.ideas.project.repository;

import com.alexsitiy.ideas.project.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Integer> {


    Optional<Reaction> findReactionByProjectId(Integer projectId);
}
