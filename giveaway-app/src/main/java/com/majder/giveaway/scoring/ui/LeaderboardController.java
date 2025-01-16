package com.majder.giveaway.scoring.ui;

import com.majder.giveaway.scoring.application.LeaderboardService;
import com.majder.giveaway.scoring.domain.MonthlyLeaderboard;
import com.majder.giveaway.useraccount.UserAccount;
import com.majder.giveaway.useraccount.web.Authenticated;
import com.majder.giveaway.utils.annotation.Log;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @GetMapping("/{month}")
    @Log(message = "Fetching top users for the month")
    public ResponseEntity<List<MonthlyLeaderboard.Winner>> getTopUsers(@PathVariable String month) {
        List<MonthlyLeaderboard.Winner> topUsers = leaderboardService.getTopUsers(month);
        return ResponseEntity.ok(topUsers);
    }

    @GetMapping("/user-points")
    @Log(message = "Fetching points for user")
    public ResponseEntity<Integer> getUserPoints(@Authenticated UserAccount userAccount) {
        Integer points = leaderboardService.getUserPoints(userAccount.email(), YearMonth.now());
        return ResponseEntity.ok(points);
    }
}
