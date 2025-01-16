package com.majder.giveaway.workout.training.application;

import com.majder.giveaway.workout.WorkoutPerformed;
import com.majder.giveaway.workout.gym.domain.GymRepository;
import com.majder.giveaway.workout.training.domain.Training;
import com.majder.giveaway.workout.training.domain.TrainingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher events;
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
//        MonthlyLeaderboard monthlyLeaderboard = new MonthlyLeaderboard(); // todo ss
        System.out.println("trainingRepository.findAll().size()");
        System.out.println(trainingRepository.findAll().size());
        Training activeVisit = trainingRepository.findActiveTrainingByUserAndGym(userEmail, gymId)
                .orElseThrow(() -> new IllegalArgumentException("No active visit found for this user at the specified gym"));

        activeVisit.checkOut(LocalDateTime.now());

        trainingRepository.save(activeVisit);

        if (activeVisit.getWorkoutLengthInMinutes() > 45) {
            events.publishEvent(new WorkoutPerformed(userEmail, activeVisit.getWorkoutLengthInMinutes(), gymId));
        }
    }
}