package com.alexsitiy.ideas.project.repository;

import com.alexsitiy.ideas.project.entity.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    Optional<Project> findByTitle(String title);

    @EntityGraph(attributePaths = "user")
    @Query("SELECT p FROM Project p WHERE p.id = :id")
    Optional<Project> findByIdWithUser(Integer id);
}
