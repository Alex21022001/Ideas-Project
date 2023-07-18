package com.alexsitiy.ideas.project.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "project")
@EqualsAndHashCode(exclude = "project")
@Data
@Builder
@Entity
@Table(name = "project_status")
public class ProjectStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne(optional = false)
    @JoinColumn(name = "project_id", unique = true, nullable = false)
    private Project project;
}
