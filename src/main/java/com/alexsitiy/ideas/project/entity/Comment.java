package com.alexsitiy.ideas.project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_comment")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"user", "project"})
@EqualsAndHashCode(exclude = {"user", "project"})
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(name = "comment_type",nullable = false)
    private CommentType type;
}
