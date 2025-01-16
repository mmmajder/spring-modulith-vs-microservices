package org.example.referral.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.events.ReferralRewardedEvent;
import org.example.events.WorkoutPerformedEvent;
import org.example.referral.domain.Referral;
import org.example.referral.domain.ReferralRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class ReferralService {

    private final ReferralRepository referralRepository;
    private final KafkaTemplate<String, ReferralRewardedEvent> kafkaTemplate;

    @KafkaListener(topics = "workout-performed")
    public void on(WorkoutPerformedEvent event) {
        List<Referral> referrals = referralRepository.findByReferredEmail(event.getUserEmail());
        for (Referral referral : referrals) {
            if (!referral.isRewarded()) {
                rewardReferral(referral);
            }
        }
    }

    private void rewardReferral(Referral referral) {
        referral.reward();
        referralRepository.save(referral);
        kafkaTemplate.send("referral-rewarded", new ReferralRewardedEvent(referral.getReferrerEmail(), referral.getReferredEmail(), referral.getReferralDate()));
    }

    public Referral createReferral(String referrerEmail, ReferralDto referralDto) {
        Referral referral = new Referral(referrerEmail, referralDto.referredEmail(), LocalDateTime.now());
        return referralRepository.save(referral);
    }

    public List<Referral> getReferralsByReferrer(String referrerEmail) {
        return referralRepository.findByReferrerEmail(referrerEmail);
    }
}
