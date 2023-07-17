package com.alexsitiy.ideas.project.repository.custom;

import com.alexsitiy.ideas.project.entity.HistoryEntity;
import com.alexsitiy.ideas.project.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectHistoryRepository {

    Page<HistoryEntity<Project>> findAllProjectHistoryByUsername(String username, Pageable pageable);
}
