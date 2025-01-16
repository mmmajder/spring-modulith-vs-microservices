package com.majder.giveaway.workout.training.ui;

import com.majder.giveaway.useraccount.UserAccount;
import com.majder.giveaway.useraccount.web.Authenticated;
import com.majder.giveaway.workout.training.application.WorkoutService;
import com.majder.giveaway.utils.annotation.Log;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/workout")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;

    @PostMapping("/check-in")
    @Log(message = "User check-in attempt")
    public ResponseEntity<String> checkInUser(@Authenticated UserAccount userAccount, @RequestParam Long gymId) {
        try {
            workoutService.checkInUser(userAccount.email(), gymId, LocalDateTime.now());
            return ResponseEntity.ok("User successfully checked into the gym");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @PostMapping("/check-out")
    @Log(message = "User check-out attempt")
    public ResponseEntity<String> checkOutUser(@Authenticated UserAccount userAccount, @RequestParam Long gymId) {
        try {
            workoutService.checkOutUser(userAccount.email(), gymId);
            return ResponseEntity.ok("User successfully checked out from the gym");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
