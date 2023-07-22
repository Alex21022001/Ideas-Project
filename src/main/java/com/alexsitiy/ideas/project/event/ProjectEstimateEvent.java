package com.alexsitiy.ideas.project.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ProjectEstimateEvent extends ApplicationEvent {

    private final Integer projectId;

    public ProjectEstimateEvent(Object source) {
        super(source);
        projectId = (Integer) source;
    }
}
