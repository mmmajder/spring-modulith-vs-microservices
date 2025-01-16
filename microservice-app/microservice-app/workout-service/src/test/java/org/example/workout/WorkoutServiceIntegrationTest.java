package org.example.workout;

import org.example.events.WorkoutPerformedEvent;
import org.example.training.application.WorkoutService;
import org.example.training.domain.Training;
import org.example.training.domain.TrainingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class WorkoutServiceIntegrationTest {

    @DynamicPropertySource
    static void initializeData(DynamicPropertyRegistry registry) {
        registry.add("spring.sql.init.data-locations", () -> "classpath:workout-data.sql");
    }

    @Autowired
    private WorkoutService workoutService;

    @Autowired
    private TrainingRepository trainingRepository;

    @Test
    void shouldCheckInUserSuccessfully() {
        String userEmail = "john.wick2@continental.com";
        Long gymId = 2L;

        workoutService.checkInUser(userEmail, gymId, LocalDateTime.now());

        // Assert that the user has an active training session
        assertThat(trainingRepository.findActiveTrainingsByUser(userEmail))
                .isNotEmpty()
                .anyMatch(training -> training.getGymId().equals(gymId));
    }

    @Test
    void shouldThrowExceptionWhenGymNotFound() {
        Long invalidGymId = 999L;
        String userEmail = "john.wick@continental.com";

        assertThatThrownBy(() -> workoutService.checkInUser(userEmail, invalidGymId, LocalDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Gym not found");
    }

//    @Test
//    void shouldPublishEventOnCheckOut() {
//        String userEmail = "user1@example.com";
//        Long gymId = 1L;
//
//        // Assume the user is already checked in
//        Training training = new Training(userEmail, gymId, LocalDateTime.now());
//        trainingRepository.save(training);
//
//        WorkoutPerformedEvent event = workoutService.checkOutUser(userEmail, gymId);
//
//        // Assert the event was published with the correct details
//        assertThat(event).isNotNull();
//        assertThat(event.userEmail()).isEqualTo(userEmail);
//        assertThat(event.minutes()).isGreaterThanOrEqualTo(45);
//    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyCheckedIn() {
        Long firstGymId = 1L;
        Long secondGymId = 2L;
        String userEmail = "john.wick@continental.com";

        // Simulate an active training session for the user at the first gym
        Training activeTraining = new Training(userEmail, firstGymId, LocalDateTime.now());
        trainingRepository.save(activeTraining);

        // Attempt to check in the user at a different gym
        assertThatThrownBy(() -> workoutService.checkInUser(userEmail, secondGymId, LocalDateTime.now()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("User is already checked into another gym");
    }
}
