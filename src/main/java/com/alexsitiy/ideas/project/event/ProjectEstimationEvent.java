package com.alexsitiy.ideas.project.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ProjectEstimationEvent extends ApplicationEvent {

    private final Integer projectId;
    private final Integer callerId;

    public ProjectEstimationEvent(Object source, Integer callerId) {
        super(source);
        projectId = (Integer) source;
        this.callerId = callerId;
    }
}
