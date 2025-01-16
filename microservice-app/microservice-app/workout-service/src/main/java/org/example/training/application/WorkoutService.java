package org.example.training.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.events.WorkoutPerformedEvent;
import org.example.gym.domain.GymRepository;
import org.example.training.domain.Training;
import org.example.training.domain.TrainingRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final GymRepository gymRepository;
    private final TrainingRepository trainingRepository;
    private final KafkaTemplate<String, WorkoutPerformedEvent> kafkaTemplate;
    public void checkInUser(String userEmail, Long gymId, LocalDateTime timeOfWorkout) {
        if (!gymRepository.existsById(gymId))
            throw new IllegalArgumentException("Gym not found");

        List<Training> activeVisits = trainingRepository.findActiveTrainingsByUser(userEmail);
        if (!activeVisits.isEmpty()) {
            throw new IllegalStateException("User is already checked into another gym");
        }

        Training visit = new Training(userEmail, gymId, timeOfWorkout);
        trainingRepository.save(visit);
    }

    public void checkOutUser(String userEmail, Long gymId) {
        Training activeVisit = trainingRepository.findActiveTrainingByUserAndGym(userEmail, gymId)
                .orElseThrow(() -> new IllegalArgumentException("No active visit found for this user at the specified gym"));

        activeVisit.checkOut(LocalDateTime.now());

        trainingRepository.save(activeVisit);

        if (activeVisit.getWorkoutLengthInMinutes() > 45) {       // CHECK return
            kafkaTemplate.send("workout-performed", new WorkoutPerformedEvent(userEmail, activeVisit.getWorkoutLengthInMinutes(), gymId));
        }
    }
}
