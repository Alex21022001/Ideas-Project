package com.alexsitiy.ideas.project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"project", "expert"})
@EqualsAndHashCode(exclude = {"project", "expert"})
@Data
@Builder
@Entity
@Table(name = "project_status")
@OptimisticLocking(type = OptimisticLockType.VERSION)
public class ProjectStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", unique = true, nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expert_id")
    private User expert;

    @Version
    private Integer version;
}
