package org.example.workout;

import org.example.events.WorkoutPerformedEvent;
import org.example.gym.domain.GymRepository;
import org.example.training.application.WorkoutService;
import org.example.training.domain.Training;
import org.example.training.domain.TrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutServiceTest {

    private WorkoutService workoutService;

    @Mock
    private GymRepository gymRepository;

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private KafkaTemplate<String, WorkoutPerformedEvent> kafkaTemplate;

    @BeforeEach
    void setUp() {
        workoutService = new WorkoutService(gymRepository, trainingRepository, kafkaTemplate);
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
        verify(kafkaTemplate).send(eq("workout-performed"), any(WorkoutPerformedEvent.class));
    }

    @Test
    void checkOutUserDoesNotPublishEventIfWorkoutLengthIsTooShort() {
        String userEmail = "john.doe@example.com";
        Long gymId = 1L;
        LocalDateTime checkInTime = LocalDateTime.now().minusMinutes(25);
        Training activeVisit = new Training(userEmail, gymId, checkInTime);
        activeVisit.checkOut(LocalDateTime.now());

        when(trainingRepository.findActiveTrainingByUserAndGym(userEmail, gymId)).thenReturn(Optional.of(activeVisit));
        when(trainingRepository.save(any(Training.class))).thenReturn(activeVisit);

        workoutService.checkOutUser(userEmail, gymId);

        verify(kafkaTemplate, never()).send(anyString(), any(WorkoutPerformedEvent.class));
    }
}
