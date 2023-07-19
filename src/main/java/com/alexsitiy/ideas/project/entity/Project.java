package com.alexsitiy.ideas.project.entity;

import com.alexsitiy.ideas.project.listener.ProjectDefaultValueListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table
@ToString(exclude = {"user", "comments"})
@EqualsAndHashCode(exclude = {"user", "comments"})
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@EntityListeners({ProjectDefaultValueListener.class})
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "image_path", nullable = false)
    private String imagePath;

    @Column(name = "docs_path")
    private String docPath;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotAudited
    @OneToOne(mappedBy = "project", fetch = FetchType.EAGER, optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private ProjectStatus status;

    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotAudited
    @OneToOne(mappedBy = "project", optional = false, cascade = CascadeType.PERSIST)
    private ProjectReaction reaction;

    @NotAudited
    @Builder.Default
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();
}
