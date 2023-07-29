package com.alexsitiy.ideas.project.event;

import com.alexsitiy.ideas.project.entity.CommentType;
import com.alexsitiy.ideas.project.entity.Status;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ProjectCommentedEvent extends ApplicationEvent {

    private final Integer projectId;
    private final Integer callerId;
    private final CommentType commentType;

    public ProjectCommentedEvent(Object source, Integer callerId, CommentType commentType) {
        super(source);
        projectId = (Integer) source;
        this.callerId = callerId;
        this.commentType = commentType;
    }
}
