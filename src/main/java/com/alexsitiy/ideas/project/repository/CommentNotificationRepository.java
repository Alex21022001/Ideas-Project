package com.alexsitiy.ideas.project.repository;

import com.alexsitiy.ideas.project.entity.CommentNotification;
import com.alexsitiy.ideas.project.entity.Notification;
import com.alexsitiy.ideas.project.entity.StatusNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentNotificationRepository extends JpaRepository<CommentNotification,Integer> {

    Optional<CommentNotification> findByProjectIdAndCallerId(Integer projectId, Integer callerId);
}
