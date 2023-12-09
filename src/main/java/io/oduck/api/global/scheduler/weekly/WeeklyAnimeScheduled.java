package io.oduck.api.global.scheduler.weekly;

import io.oduck.api.domain.weekly.repository.WeeklyAnimeRepository;
import io.oduck.api.domain.weekly.service.WeeklyAnimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Component
@EnableScheduling
public class WeeklyAnimeScheduled {
    private final WeeklyAnimeService weeklyAnimeService;

    @Scheduled(cron = "0 0 4 * * MON")
    public void resetWeeklyAnime() {
        sleep();
        weeklyAnimeService.reset();
        log.info("Weekly anime reset complete");
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("스케쥴러 비정상", e);
            throw new RuntimeException(e);
        }
    }
}
