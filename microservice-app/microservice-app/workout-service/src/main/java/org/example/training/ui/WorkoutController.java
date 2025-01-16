package org.example.training.ui;

import lombok.RequiredArgsConstructor;
import org.example.training.application.WorkoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/workout-service/workout")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;

    @PostMapping("/check-in")
    public ResponseEntity<String> checkInUser(@RequestHeader("X-User-Email") String email, @RequestParam Long gymId) {
        try {
            System.out.println("caoo kralju");
            workoutService.checkInUser(email, gymId, LocalDateTime.now());
            return ResponseEntity.ok("User successfully checked into the gym");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @PostMapping("/check-out")
    public ResponseEntity<String> checkOutUser(@RequestHeader("X-User-Email") String email, @RequestParam Long gymId) {
        try {
            workoutService.checkOutUser(email, gymId);
            return ResponseEntity.ok("User successfully checked out from the gym");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
