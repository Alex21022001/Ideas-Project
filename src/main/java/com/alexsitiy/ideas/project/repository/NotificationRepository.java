package com.alexsitiy.ideas.project.repository;

import com.alexsitiy.ideas.project.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

}
