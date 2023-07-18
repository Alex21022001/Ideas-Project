package com.alexsitiy.ideas.project.listener;

import com.alexsitiy.ideas.project.entity.Project;
import com.alexsitiy.ideas.project.entity.ProjectStatus;
import com.alexsitiy.ideas.project.entity.Reaction;
import com.alexsitiy.ideas.project.entity.Status;
import jakarta.persistence.PrePersist;

import java.time.Instant;

public class ProjectDefaultValueListener {

    @PrePersist
    public void setDefaultValues(Project project) {
        ProjectStatus projectStatus = ProjectStatus
                .builder()
                .status(Status.IN_PROGRESS)
                .project(project)
                .build();
        Reaction reaction = new Reaction();

        reaction.setProject(project);
        project.setStatus(projectStatus);
        project.setCreatedAt(Instant.now());
    }

}
