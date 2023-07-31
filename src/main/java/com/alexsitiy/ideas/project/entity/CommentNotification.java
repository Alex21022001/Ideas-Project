package com.alexsitiy.ideas.project.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("COMMENT")
public class CommentNotification extends Notification{

    @Enumerated(EnumType.STRING)
    @Column(name = "comment")
    private CommentType commentType;
}
