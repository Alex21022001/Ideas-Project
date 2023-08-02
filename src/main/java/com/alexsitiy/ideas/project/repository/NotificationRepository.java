package com.alexsitiy.ideas.project.repository;

import com.alexsitiy.ideas.project.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    Page<Notification> findAllByUserId(Integer userId, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Notification n SET n.stale = true WHERE n.id in (:ids) AND n.user.id = :userId")
    int makeNotificationsStale(List<Integer> ids, Integer userId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Notification n WHERE n.stale = true AND n.createdAt < :before")
    int deleteAllByStaleTrueAndCreatedAtBefore(Instant before);
}
