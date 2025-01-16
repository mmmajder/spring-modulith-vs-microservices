package com.majder.giveaway.workout;

import com.majder.giveaway.workout.training.application.WorkoutService;
import com.majder.giveaway.workout.training.domain.Training;
import com.majder.giveaway.workout.training.domain.TrainingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@ApplicationModuleTest
class WorkoutServiceIntegrationTest {

    @DynamicPropertySource
    static void initializeData(DynamicPropertyRegistry registry) {
        registry.add("spring.sql.init.data-locations", () -> "classpath:workout-data.sql");
    }

    @Autowired
    WorkoutService workoutService;

    @Autowired
    TrainingRepository trainingRepository;

    @Test
    void shouldCheckInUserSuccessfully(Scenario scenario) {
        String userEmail = "john.wick2@continental.com";
        Long gymId = 2L;
        // Stimulate a scenario where user checks in
        scenario.stimulate(() -> workoutService.checkInUser(userEmail, gymId, LocalDateTime.now()))
                .andWaitForStateChange(() -> trainingRepository.findActiveTrainingsByUser(userEmail))
                .andVerify(trainings -> assertThat(trainings).isNotEmpty());
    }

    @Test
    void shouldThrowExceptionWhenGymNotFound(Scenario scenario) {
        Long invalidGymId = 999L;
        String userEmail = "john.wick@continental.com";

        assertThatThrownBy(() -> workoutService.checkInUser(userEmail, invalidGymId, LocalDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Gym not found");
    }

    @Test
    void shouldPublishEventOnCheckOut(Scenario scenario) {
        String userEmail = "user1@example.com";
        Long gymId = 1L;

        scenario.stimulate(() -> workoutService.checkOutUser(userEmail, gymId))
                .andWaitForEventOfType(WorkoutPerformed.class)
                .toArriveAndVerify((event, dto) -> {
                    assertThat(event.userEmail()).isEqualTo(userEmail);
                    assertThat(event.minutes()).isGreaterThanOrEqualTo(45);
                });
    }


    @Test
    void shouldThrowExceptionWhenUserAlreadyCheckedIn(Scenario scenario) {
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
