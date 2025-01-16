package com.majder.giveaway.scoring.application;

import com.majder.giveaway.referral.ReferralRewarded;
import com.majder.giveaway.scoring.domain.*;
import com.majder.giveaway.workout.WorkoutPerformed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
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


    @ApplicationModuleListener
    public void on(WorkoutPerformed event) {
        addPoints(event.userEmail(), POINTS_FOR_WORKOUT, "Workout " + event.minutes() + " mins");
    }

    @ApplicationModuleListener
    public void on(ReferralRewarded event) {
        addPoints(event.referrerEmail(), POINTS_FOR_REFERRAL, event.referrerEmail() + " invited " + event.referredEmail() + " to the app!");
    }

    private void addPoints(String userEmail, int points, String description) {
        ScoringRecord record = new ScoringRecord(userEmail, points, description, LocalDateTime.now());
        scoringRecordRepository.save(record);
    }
}