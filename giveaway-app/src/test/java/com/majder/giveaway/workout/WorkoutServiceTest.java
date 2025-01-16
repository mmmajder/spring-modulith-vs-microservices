package com.majder.giveaway.workout;

import com.majder.giveaway.workout.gym.domain.GymRepository;
import com.majder.giveaway.workout.training.application.WorkoutService;
import com.majder.giveaway.workout.training.domain.Training;
import com.majder.giveaway.workout.training.domain.TrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutServiceTest {

    WorkoutService workoutService;

    @Mock
    GymRepository gymRepository;

    @Mock
    TrainingRepository trainingRepository;

    @Mock
    ApplicationEventPublisher events;

    @BeforeEach
    void setUp() {
        workoutService = new WorkoutService(gymRepository, trainingRepository, events);
    }

    @Test
    void checkInUserSuccessfully() {
        String userEmail = "john.doe@example.com";
        Long gymId = 1L;
        LocalDateTime timeOfWorkout = LocalDateTime.now();

        when(gymRepository.existsById(gymId)).thenReturn(true);
        when(trainingRepository.findActiveTrainingsByUser(userEmail)).thenReturn(List.of());

        workoutService.checkInUser(userEmail, gymId, timeOfWorkout);

        verify(trainingRepository).save(any(Training.class));
    }

    @Test
    void checkInUserThrowsExceptionIfGymNotFound() {
        String userEmail = "john.doe@example.com";
        Long gymId = 1L;
        LocalDateTime timeOfWorkout = LocalDateTime.now();

        when(gymRepository.existsById(gymId)).thenReturn(false);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> workoutService.checkInUser(userEmail, gymId, timeOfWorkout))
                .withMessage("Gym not found");

        verify(trainingRepository, never()).save(any());
    }

    @Test
    void checkInUserThrowsExceptionIfAlreadyCheckedIn() {
        String userEmail = "john.doe@example.com";
        Long gymId = 1L;
        LocalDateTime timeOfWorkout = LocalDateTime.now();

        when(gymRepository.existsById(gymId)).thenReturn(true);
        when(trainingRepository.findActiveTrainingsByUser(userEmail)).thenReturn(List.of(new Training(userEmail, gymId, timeOfWorkout)));

        assertThatIllegalStateException()
                .isThrownBy(() -> workoutService.checkInUser(userEmail, gymId, timeOfWorkout))
                .withMessage("User is already checked into another gym");

        verify(trainingRepository, never()).save(any());
    }

    @Test
    void checkOutUserSuccessfullyAndPublishEvent() {
        String userEmail = "john.doe@example.com";
        Long gymId = 1L;
        LocalDateTime checkInTime = LocalDateTime.now().minusMinutes(50);
        Training activeVisit = new Training(userEmail, gymId, checkInTime);

        when(trainingRepository.findActiveTrainingByUserAndGym(userEmail, gymId)).thenReturn(Optional.of(activeVisit));
        when(trainingRepository.save(any(Training.class))).thenReturn(activeVisit);

        workoutService.checkOutUser(userEmail, gymId);

        verify(trainingRepository).save(activeVisit);
        verify(events).publishEvent(any(WorkoutPerformed.class));
    }

    @Test
    void checkOutUserThrowsExceptionIfNoActiveVisitFound() {
        String userEmail = "john.doe@example.com";
        Long gymId = 1L;

        when(trainingRepository.findActiveTrainingByUserAndGym(userEmail, gymId)).thenReturn(Optional.empty());

        assertThatIllegalArgumentException()
                .isThrownBy(() -> workoutService.checkOutUser(userEmail, gymId))
                .withMessage("No active visit found for this user at the specified gym");

        verify(trainingRepository, never()).save(any());
    }

    @Test
    void checkOutUserDoesNotPublishEventIfWorkoutLengthIsTooShort() {
        String userEmail = "john.doe@example.com";
        Long gymId = 1L;
        LocalDateTime checkInTime = LocalDateTime.now().minusMinutes(30);
        Training activeVisit = new Training(userEmail, gymId, checkInTime);
        activeVisit.checkOut(LocalDateTime.now());

        when(trainingRepository.findActiveTrainingByUserAndGym(userEmail, gymId)).thenReturn(Optional.of(activeVisit));
        when(trainingRepository.save(any(Training.class))).thenReturn(activeVisit);

        workoutService.checkOutUser(userEmail, gymId);

        verify(events, never()).publishEvent(any(WorkoutPerformed.class));
    }
}
