package com.alexsitiy.ideas.project.repository;

import com.alexsitiy.ideas.project.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    Page<Notification> findAllByUserId(Integer userId, Pageable pageable);
}
