package com.alexsitiy.ideas.project.repository;

import com.alexsitiy.ideas.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project,Integer> {

    Optional<Project> findByTitle(String title);
}
