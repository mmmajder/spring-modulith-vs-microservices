package org.example.scoring.ui;

import lombok.RequiredArgsConstructor;
import org.example.scoring.application.LeaderboardService;
import org.example.scoring.domain.MonthlyLeaderboard;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/scoring-service/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    /**
     * Get the top users for a specific month.
     *
     * @param month the month in "YYYY-MM" format
     * @return a list of top users
     */
    @GetMapping("/{month}")
    public ResponseEntity<List<MonthlyLeaderboard.Winner>> getTopUsers(@PathVariable String month) {
        List<MonthlyLeaderboard.Winner> topUsers = leaderboardService.getTopUsers(month);
        return ResponseEntity.ok(topUsers);
    }

    /**
     * Get the points for a specific user in the last month.
     *
     * @return the total points
     */
    @GetMapping("/user-points")
    public ResponseEntity<Integer> getUserPoints(@RequestHeader("X-User-Email") String email) {
        Integer points = leaderboardService.getUserPoints(email, YearMonth.now());
        return ResponseEntity.ok(points);
    }
}
