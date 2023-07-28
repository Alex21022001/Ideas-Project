package com.alexsitiy.ideas.project.listener;

import com.alexsitiy.ideas.project.entity.BasicAuditEntity;
import jakarta.persistence.PrePersist;

import java.time.Instant;

public class CreateAtSetListener {

    @PrePersist
    public void setCreatedAt(BasicAuditEntity basicAuditEntity){
        basicAuditEntity.setCreatedAt(Instant.now());
    }
}
