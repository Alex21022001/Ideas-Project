package com.alexsitiy.ideas.project.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = "project")
@EqualsAndHashCode(exclude = "project")
@Entity
@Table(name = "project_reaction")
public class ProjectReaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "likes", nullable = false)
    private Integer likes = 0;

    @Column(name = "dislikes", nullable = false)
    private Integer dislikes = 0;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", unique = true, nullable = false)
    private Project project;


    public void setProject(Project project) {
        this.project = project;
        project.setReaction(this);
    }

    public void increment(CommentType commentType) {
        if (commentType == CommentType.LIKE) {
            likes++;
        } else {
            dislikes++;
        }
    }

    public void decrement(CommentType commentType) {
        if (commentType == CommentType.LIKE) {
            likes--;
        } else {
            dislikes--;
        }
    }

    public void change(CommentType commentType) {
        if (commentType == CommentType.LIKE) {
            likes++;
            dislikes--;
        } else {
            dislikes++;
            likes--;
        }
    }
}
