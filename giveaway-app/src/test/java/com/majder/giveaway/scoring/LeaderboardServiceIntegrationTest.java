package com.majder.giveaway.scoring;

import com.majder.giveaway.scoring.application.LeaderboardService;
import com.majder.giveaway.scoring.domain.*;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.YearMonth;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//@Transactional
@ApplicationModuleTest
class LeaderboardServiceIntegrationTest {

    @DynamicPropertySource
    static void initializeData(DynamicPropertyRegistry registry) {
        registry.add("spring.sql.init.data-locations", () -> "classpath:leaderboard-data.sql");
    }

    @Autowired
    ScoringRecordRepository scoringRecordRepository;

    @Autowired
    MonthlyLeaderboardRepository monthlyLeaderboardRepository;

    LeaderboardService leaderboardService;

    @BeforeEach
    void setUp() {
        leaderboardService = new LeaderboardService(scoringRecordRepository, monthlyLeaderboardRepository);
    }

    @Test
    void shouldGenerateMonthlyLeaderboardSuccessfullyWithoutPublishingEvent(Scenario scenario) {
        YearMonth currentMonth = YearMonth.of(2024, 12);
        String month = currentMonth.toString();

        scenario.stimulate(() -> leaderboardService.generateMonthlyLeaderboard(currentMonth))
                .andWaitForStateChange(() -> monthlyLeaderboardRepository.findByLeaderboardMonth(month))
                .andVerify(leaderboard -> {
                    assertThat(leaderboard).isNotNull();
                    leaderboard.ifPresent(l -> Hibernate.initialize(l.getWinners()));

                    assertThat(leaderboard.isPresent()).isTrue();
                    assertThat(leaderboard.get().getWinners()).hasSize(3);
                    assertThat(leaderboard.get().getWinners().get(0).email()).isEqualTo("user1@example.com");
                    assertThat(leaderboard.get().getWinners().get(1).email()).isEqualTo("user2@example.com");
                    assertThat(leaderboard.get().getWinners().get(2).email()).isEqualTo("user3@example.com");
                });
    }


    @Test
    void getScoresForAllUsersReturnsCorrectScores(Scenario scenario) {
        YearMonth currentMonth = YearMonth.of(2024, 12);

        List<Object[]> scoresList = leaderboardService.getScoresForAllUsers(currentMonth);

        assertThat(scoresList).hasSize(3);
        assertThat(scoresList.get(0)[0]).isEqualTo("user1@example.com");
        assertThat(scoresList.get(0)[1]).isEqualTo(350L);
        assertThat(scoresList.get(1)[0]).isEqualTo("user2@example.com");
        assertThat(scoresList.get(1)[1]).isEqualTo(120L);
        assertThat(scoresList.get(2)[0]).isEqualTo("user3@example.com");
        assertThat(scoresList.get(2)[1]).isEqualTo(100L);
    }

    @Test
    void getTopUsersReturnsCorrectTopUsers(Scenario scenario) {
        String currentMonth = "2024-12";
        List<MonthlyLeaderboard.Winner> topUsers = leaderboardService.getTopUsers(currentMonth);

        assertThat(topUsers).hasSize(3);
        assertThat(topUsers.get(0).email()).isEqualTo("user1@example.com");
        assertThat(topUsers.get(0).points()).isEqualTo(350L);
        assertThat(topUsers.get(1).email()).isEqualTo("user2@example.com");
        assertThat(topUsers.get(1).points()).isEqualTo(120L);
        assertThat(topUsers.get(2).email()).isEqualTo("user3@example.com");
        assertThat(topUsers.get(2).points()).isEqualTo(100L);
    }

    @Test
    void getUserPointsReturnsCorrectPointsForUser(Scenario scenario) {
        Integer points = leaderboardService.getUserPoints("user1@example.com", YearMonth.of(2024, 12));
        assertThat(points).isEqualTo(350);
    }
}
