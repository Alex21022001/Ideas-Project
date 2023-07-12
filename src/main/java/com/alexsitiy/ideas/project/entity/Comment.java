package com.alexsitiy.ideas.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

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
    @Column(name = "comment_type", nullable = false)
    private CommentType type;

    @Column(name = "commented_at", nullable = false)
    private Instant commentedAt;

    @PrePersist
    void prePersist() {
        if (this.type.equals(CommentType.LIKE)) {
            this.project.setLikes(this.project.getLikes() + 1);
        } else if (this.type.equals(CommentType.DISLIKE)) {
            this.project.setDislikes(this.project.getDislikes() + 1);
        }

        this.setCommentedAt(Instant.now());
    }

    @PreUpdate
    void preUpdate() {
        if (this.type.equals(CommentType.LIKE)) {
            this.project.setLikes(this.project.getLikes() + 1);
            this.project.setDislikes(this.project.getDislikes() - 1);
        } else if (this.type.equals(CommentType.DISLIKE)) {
            this.project.setDislikes(this.project.getDislikes() + 1);
            this.project.setLikes(this.project.getLikes() - 1);
        }

        this.setCommentedAt(Instant.now());
    }

    @PreRemove
    void preRemove() {
        if (this.type.equals(CommentType.LIKE)) {
            this.project.setLikes(this.project.getLikes() - 1);
        } else if (this.type.equals(CommentType.DISLIKE)) {
            this.project.setDislikes(this.project.getDislikes() - 1);
        }
    }

    public static Comment of(Project project, User user, CommentType commentType) {
        Comment comment = new Comment();
        comment.setType(commentType);
        comment.setUser(user);
        comment.setProject(project);
        return comment;
    }
}
