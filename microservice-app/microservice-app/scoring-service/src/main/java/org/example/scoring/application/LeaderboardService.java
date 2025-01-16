package org.example.scoring.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.scoring.domain.MonthlyLeaderboard;
import org.example.scoring.domain.MonthlyLeaderboardRepository;
import org.example.scoring.domain.ScoringRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class LeaderboardService {

    private final ScoringRecordRepository scoringRecordRepository;
    private final MonthlyLeaderboardRepository monthlyLeaderboardRepository;

    @Transactional
    public void generateMonthlyLeaderboard(YearMonth currentMonth) {
        String month = currentMonth.toString();

        Optional<MonthlyLeaderboard> existingLeaderboard = monthlyLeaderboardRepository.findByLeaderboardMonth(month);

        List<Object[]> scoresList = getScoresForAllUsers(currentMonth);

        List<MonthlyLeaderboard.Winner> winners = scoresList.stream()
                .map(row -> new MonthlyLeaderboard.Winner((String) row[0], ((Number) row[1]).intValue())) // Map email and points to Winner
                .sorted((w1, w2) -> Integer.compare(w2.points(), w1.points())) // Sort by points descending
                .limit(10)
                .collect(Collectors.toList());

        MonthlyLeaderboard leaderboard;
        if (existingLeaderboard.isPresent()) {
            leaderboard = existingLeaderboard.get();
            leaderboard.setTopUsers(winners);
        } else {
            leaderboard = new MonthlyLeaderboard(month, winners);
        }

        monthlyLeaderboardRepository.save(leaderboard);
    }

    public List<Object[]> getScoresForAllUsers(YearMonth month) {
        return scoringRecordRepository.findTotalPointsByMonth(month.getYear(), month.getMonthValue());
    }


    public List<MonthlyLeaderboard.Winner> getTopUsers(String month) {
        return monthlyLeaderboardRepository.findByLeaderboardMonthWithWinners(month)
                .map(MonthlyLeaderboard::getWinners)
                .orElse(Collections.emptyList());
    }

    public Integer getUserPoints(String userEmail, YearMonth currentMonth) {
        return scoringRecordRepository.findTotalPointsByUserEmailByMonth(userEmail, currentMonth.getYear(), currentMonth.getMonthValue());
    }
}
