package org.example.scoring.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MonthlyLeaderboardRepository extends JpaRepository<MonthlyLeaderboard, Long> {
    Optional<MonthlyLeaderboard> findByLeaderboardMonth(String leaderboardMonth);
    @Query("SELECT m FROM MonthlyLeaderboard m JOIN FETCH m.winners WHERE m.leaderboardMonth = :leaderboardMonth")
    Optional<MonthlyLeaderboard> findByLeaderboardMonthWithWinners(@Param("leaderboardMonth") String leaderboardMonth);

}
