package com.alexsitiy.ideas.project.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ProjectCommentDeletedEvent extends ApplicationEvent {

    private final Integer projectId;
    private final Integer callerId;


    public ProjectCommentDeletedEvent(Object source, Integer callerId) {
        super(source);
        projectId = (Integer) source;
        this.callerId = callerId;
    }
}
