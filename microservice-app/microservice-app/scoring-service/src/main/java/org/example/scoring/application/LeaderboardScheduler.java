package org.example.scoring.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.events.MultipleNotificationEvent;
import org.example.events.SingleNotificationEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class LeaderboardScheduler {

    private final LeaderboardService leaderboardService;
    private final KafkaTemplate<String, MultipleNotificationEvent> kafkaTemplate;


    /**
     * Scheduled task to generate the monthly leaderboard daily at a specific time.
     * Example: Runs every day at 2:00 AM.
     */
//    @Scheduled(cron = "0 0 2 * * ?")
    @Scheduled(cron = "*/30 * * * * ?")
    public void scheduleMonthlyLeaderboardGeneration() {
        log.info("Starting scheduled task: Generate Monthly Leaderboard");
        leaderboardService.generateMonthlyLeaderboard(YearMonth.now());
        log.info("Completed scheduled task: Generate Monthly Leaderboard");
    }

//    @Scheduled(cron = "0 0 9 * * MON")
    @Scheduled(cron = "*/15 * * * * ?")
    public void performWeeklyTask() {
        log.info("Starting scheduled task: Weekly Task");
        YearMonth currentMonth = YearMonth.now();

        List<Object[]> scores = leaderboardService.getScoresForAllUsers(currentMonth);
        List<SingleNotificationEvent> notificationEvents = scores
                .stream()
                .map(row -> new SingleNotificationEvent(row[0].toString(), "Weekly workout points report!", "Congratulations you received " + row[1].toString() + " this month"))
                .toList();
        kafkaTemplate.send("leaderboard-update", new MultipleNotificationEvent(notificationEvents));
        log.info("Completed scheduled task: Weekly Task");
    }
}
