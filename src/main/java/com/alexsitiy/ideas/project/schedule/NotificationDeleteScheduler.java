package com.alexsitiy.ideas.project.schedule;

import com.alexsitiy.ideas.project.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationDeleteScheduler {

    private final NotificationRepository notificationRepository;

    @Scheduled(timeUnit = TimeUnit.HOURS, fixedDelay = 6)
    @Transactional
    public void deleteStaleNotifications() {
        Instant before = Instant.now().minus(1, ChronoUnit.DAYS);
        int count = notificationRepository.deleteAllByStaleTrueAndCreatedAtBefore(before);
        log.debug("{} notifications were deleted because of being stale", count);
    }
}
