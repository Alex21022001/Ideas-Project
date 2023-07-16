package com.alexsitiy.ideas.project.listener;

import com.alexsitiy.ideas.project.entity.Revision;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class RevisionListenerImpl implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Revision revision = (Revision) revisionEntity;
        if (authentication == null) {
            revision.setUsername("none");
        } else {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            revision.setUsername(userDetails.getUsername());
        }

    }
}
