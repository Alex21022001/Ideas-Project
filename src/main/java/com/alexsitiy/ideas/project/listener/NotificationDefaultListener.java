package com.alexsitiy.ideas.project.listener;

import com.alexsitiy.ideas.project.entity.Notification;
import jakarta.persistence.PrePersist;

public class NotificationDefaultListener {

    @PrePersist
    public void initNotification(Notification notification){
        notification.setStale(false);
    }
}
