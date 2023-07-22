package com.alexsitiy.ideas.project.repository;

import com.alexsitiy.ideas.project.entity.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProjectStatusRepository extends JpaRepository<ProjectStatus, Integer> {

    Optional<ProjectStatus> findByProjectId(Integer projectId);
}
