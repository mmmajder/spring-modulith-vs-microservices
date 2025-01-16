package com.majder.giveaway.scoring.application;

import com.majder.giveaway.scoring.MultipleNotificationEvent;
import com.majder.giveaway.scoring.SingleNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.YearMonth;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
//@Component
@Transactional
@Service
@RequiredArgsConstructor
public class LeaderboardScheduler {

    private final LeaderboardService leaderboardService;
    private final ApplicationEventPublisher events;

    /**
     * Scheduled task to generate the monthly leaderboard daily at a specific time.
     * Example: Runs every day at 2:00 AM.
     */
    @Scheduled(cron = "0 0 2 * * ?")
//    @Scheduled(cron = "*/30 * * * * ?")
    public void scheduleMonthlyLeaderboardGeneration() {
        log.info("Starting scheduled task: Generate Monthly Leaderboard");
        leaderboardService.generateMonthlyLeaderboard(YearMonth.now());
        log.info("Completed scheduled task: Generate Monthly Leaderboard");
    }

    @Scheduled(cron = "0 0 9 * * MON")
//    @Scheduled(cron = "*/15 * * * * ?")
    public void performWeeklyTask() {
        log.info("Starting scheduled task: Weekly Task");
        YearMonth currentMonth = YearMonth.now();

        List<Object[]> scores = leaderboardService.getScoresForAllUsers(currentMonth);
        List<SingleNotificationEvent> notificationEvents = scores
                .stream()
                .map(row -> new SingleNotificationEvent(row[0].toString(), "Weekly workout points report!", "Congratulations you received " + row[1].toString() + " this month"))
                .toList();
        events.publishEvent(new MultipleNotificationEvent(notificationEvents));
        log.info("Completed scheduled task: Weekly Task");
    }
}
