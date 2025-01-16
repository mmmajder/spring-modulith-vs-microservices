package org.example.scoring.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.events.ReferralRewardedEvent;
import org.example.events.WorkoutPerformedEvent;
import org.example.scoring.domain.ScoringRecord;
import org.example.scoring.domain.ScoringRecordRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class ScoringService {

    private final ScoringRecordRepository scoringRecordRepository;
    private final int POINTS_FOR_WORKOUT = 10;
    private final int POINTS_FOR_REFERRAL = 30;


    @KafkaListener(topics = "workout-performed")
    public void on(WorkoutPerformedEvent event) {
        addPoints(event.getUserEmail(), POINTS_FOR_WORKOUT, "Workout " + event.getMinutes() + " mins");
    }

    // CHECK
    @KafkaListener(topics = "referral-rewarded")
    public void on(ReferralRewardedEvent event) {
        addPoints(event.getReferrerEmail(), POINTS_FOR_REFERRAL, event.getReferrerEmail() + " invited " + event.getReferredEmail() + " to the app!");
    }

    private void addPoints(String userEmail, int points, String description) {
        ScoringRecord record = new ScoringRecord(userEmail, points, description, LocalDateTime.now());
        scoringRecordRepository.save(record);
    }
}
