package com.alexsitiy.ideas.project.service;

import com.alexsitiy.ideas.project.entity.Project;
import com.alexsitiy.ideas.project.exception.NoSuchProjectException;
import com.alexsitiy.ideas.project.repository.ProjectRepository;
import com.alexsitiy.ideas.project.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public boolean verifyUserForProject(Integer projectId, Authentication authentication) {
        SecurityUser user = (SecurityUser) authentication.getPrincipal();


        Optional<Project> maybeProject = projectRepository.findById(projectId);

        if (maybeProject.isEmpty()) {
            throw new NoSuchProjectException("There is no such Project with id:" + projectId);
        }

        return maybeProject
                .filter(project -> project.getUser().getId().equals(user.getId()))
                .isPresent();
    }
}
