package com.alexsitiy.ideas.project.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,
    EXPERT;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
