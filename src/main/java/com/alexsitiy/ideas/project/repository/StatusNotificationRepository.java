package com.alexsitiy.ideas.project.repository;

import com.alexsitiy.ideas.project.entity.StatusNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusNotificationRepository extends JpaRepository<StatusNotification,Integer> {

    Optional<StatusNotification> findByProjectIdAndCallerId(Integer projectId,Integer callerId);
}
