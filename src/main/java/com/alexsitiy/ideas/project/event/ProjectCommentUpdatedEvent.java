package com.alexsitiy.ideas.project.event;

import com.alexsitiy.ideas.project.entity.CommentType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ProjectCommentUpdatedEvent extends ApplicationEvent {

    private final Integer projectId;
    private final Integer callerId;
    private final CommentType commentType;

    public ProjectCommentUpdatedEvent(Object source, Integer callerId, CommentType commentType) {
        super(source);
        projectId = (Integer) source;
        this.callerId = callerId;
        this.commentType = commentType;
    }
}
