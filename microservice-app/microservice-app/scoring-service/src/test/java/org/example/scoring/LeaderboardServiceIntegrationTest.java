package org.example.scoring;

import org.example.scoring.application.LeaderboardService;
import org.example.scoring.domain.MonthlyLeaderboard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.YearMonth;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LeaderboardServiceIntegrationTest {

    @DynamicPropertySource
    static void initializeData(DynamicPropertyRegistry registry) {
        registry.add("spring.sql.init.data-locations", () -> "classpath:leaderboard-data.sql");
    }

    @Autowired
    private LeaderboardService leaderboardService;

    @Test
    void getScoresForAllUsersReturnsCorrectScores() {
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
    void getTopUsersReturnsCorrectTopUsers() {
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
    void getUserPointsReturnsCorrectPointsForUser() {
        Integer points = leaderboardService.getUserPoints("user1@example.com", YearMonth.of(2024, 12));
        assertThat(points).isEqualTo(350);
    }
}
