package com.alexsitiy.ideas.project.repository;

import com.alexsitiy.ideas.project.entity.StatusNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusNotificationRepository extends JpaRepository<StatusNotification,Integer> {
}
