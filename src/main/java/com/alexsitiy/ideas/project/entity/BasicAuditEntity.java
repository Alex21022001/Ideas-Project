package com.alexsitiy.ideas.project.entity;

import com.alexsitiy.ideas.project.listener.CreatedAtSetListener;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.time.Instant;

@Data
@MappedSuperclass
@EntityListeners({CreatedAtSetListener.class})
public class BasicAuditEntity {

    @Column(name = "created_at")
    private Instant createdAt;
}
