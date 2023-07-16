package com.alexsitiy.ideas.project.entity;

import com.alexsitiy.ideas.project.listener.RevisionListenerImpl;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@RevisionEntity(value = RevisionListenerImpl.class)
public class Revision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    private Integer id;

    @RevisionTimestamp
    @Column(name = "timestamp", nullable = false)
    private Long timestamp;

    @Column(name = "username", nullable = false)
    private String username;
}
