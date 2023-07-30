package com.alexsitiy.ideas.project.event;

import com.alexsitiy.ideas.project.entity.Status;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ProjectEstimationEvent extends ApplicationEvent {

    private final Integer projectId;
    private final Integer callerId;
    private final Status status;

    public ProjectEstimationEvent(Object source, Integer callerId, Status status) {
        super(source);
        projectId = (Integer) source;
        this.callerId = callerId;
        this.status = status;
    }
}
