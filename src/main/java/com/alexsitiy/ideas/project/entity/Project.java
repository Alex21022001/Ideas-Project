package com.alexsitiy.ideas.project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.springframework.web.multipart.MultipartFile;

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
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "image_path", nullable = false)
    private String imagePath;

    @Column(name = "docs_path")
    private String docsPath;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(mappedBy = "project", optional = false, cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    private Reaction reaction;

    @Builder.Default
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();
}
