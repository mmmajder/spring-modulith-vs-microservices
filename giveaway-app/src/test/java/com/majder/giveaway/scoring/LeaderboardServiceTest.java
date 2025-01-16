package com.majder.giveaway.scoring;

import com.majder.giveaway.scoring.application.LeaderboardService;
import com.majder.giveaway.scoring.domain.*;
        import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeaderboardServiceTest {

    private LeaderboardService leaderboardService;

    @Mock
    private ScoringRecordRepository scoringRecordRepository;

    @Mock
    private MonthlyLeaderboardRepository monthlyLeaderboardRepository;

    @BeforeEach
    void setUp() {
        leaderboardService = new LeaderboardService(scoringRecordRepository, monthlyLeaderboardRepository);
    }

    @Test
    void generateMonthlyLeaderboard_createsLeaderboardIfNotExist() {
        YearMonth currentMonth = YearMonth.of(2025, 1);
        List<Object[]> scoresList = List.of(
                new Object[]{"john.doe@example.com", 100},
                new Object[]{"jane.smith@example.com", 120}
        );
        when(scoringRecordRepository.findTotalPointsByMonth(anyInt(), anyInt())).thenReturn(scoresList);
        when(monthlyLeaderboardRepository.findByLeaderboardMonth(any())).thenReturn(Optional.empty());

        leaderboardService.generateMonthlyLeaderboard(currentMonth);

        verify(monthlyLeaderboardRepository).save(any(MonthlyLeaderboard.class));
    }

    @Test
    void generateMonthlyLeaderboard_updatesLeaderboardIfExist() {
        YearMonth currentMonth = YearMonth.of(2025, 1);
        List<Object[]> scoresList = List.of(
                new Object[]{"john.doe@example.com", 100},
                new Object[]{"jane.smith@example.com", 120}
        );
        MonthlyLeaderboard existingLeaderboard = new MonthlyLeaderboard("2025-01", List.of());
        when(scoringRecordRepository.findTotalPointsByMonth(anyInt(), anyInt())).thenReturn(scoresList);
        when(monthlyLeaderboardRepository.findByLeaderboardMonth(any())).thenReturn(Optional.of(existingLeaderboard));

        leaderboardService.generateMonthlyLeaderboard(currentMonth);

        assertThat(existingLeaderboard.getWinners()).hasSize(2);
        verify(monthlyLeaderboardRepository).save(existingLeaderboard);
    }

    @Test
    void getTopUsers_returnsEmptyListIfNoLeaderboard() {
        String month = "2025-01";
        when(monthlyLeaderboardRepository.findByLeaderboardMonthWithWinners(month)).thenReturn(Optional.empty());
        List<MonthlyLeaderboard.Winner> winners = leaderboardService.getTopUsers(month);
        assertThat(winners).isEmpty();
    }

    @Test
    void getTopUsers_returnsWinnersIfLeaderboardExists() {
        String month = "2025-01";
        MonthlyLeaderboard leaderboard = new MonthlyLeaderboard(month, List.of(
                new MonthlyLeaderboard.Winner("john.doe@example.com", 120),
                new MonthlyLeaderboard.Winner("jane.smith@example.com", 100)
        ));
        when(monthlyLeaderboardRepository.findByLeaderboardMonthWithWinners(month)).thenReturn(Optional.of(leaderboard));

        List<MonthlyLeaderboard.Winner> winners = leaderboardService.getTopUsers(month);

        assertThat(winners).hasSize(2);
        assertThat(winners.getFirst().email()).isEqualTo("john.doe@example.com");
        assertThat(winners.getFirst().points()).isEqualTo(120);
    }

    @Test
    void getUserPoints_returnsCorrectPointsForUser() {
        YearMonth currentMonth = YearMonth.of(2025, 1);
        String userEmail = "john.doe@example.com";
        int expectedPoints = 100;
        when(scoringRecordRepository.findTotalPointsByUserEmailByMonth(userEmail, currentMonth.getYear(), currentMonth.getMonthValue()))
                .thenReturn(expectedPoints);

        Integer points = leaderboardService.getUserPoints(userEmail, currentMonth);

        assertThat(points).isEqualTo(expectedPoints);
    }
}
