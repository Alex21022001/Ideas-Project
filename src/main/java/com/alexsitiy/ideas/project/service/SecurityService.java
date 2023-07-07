package com.alexsitiy.ideas.project.service;

import com.alexsitiy.ideas.project.repository.ProjectRepository;
import com.alexsitiy.ideas.project.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public boolean verifyUserForProject(Integer projectId, Authentication authentication) {
        SecurityUser user = (SecurityUser) authentication.getPrincipal();

        return projectRepository.findById(projectId)
                .filter(project -> project.getUser().getId().equals(user.getId()))
                .isPresent();
    }
}
