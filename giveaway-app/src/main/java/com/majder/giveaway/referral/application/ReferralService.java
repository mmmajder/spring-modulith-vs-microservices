package com.majder.giveaway.referral.application;

import com.majder.giveaway.referral.ReferralRewarded;
import com.majder.giveaway.referral.domain.Referral;
import com.majder.giveaway.referral.domain.ReferralRepository;
import com.majder.giveaway.workout.WorkoutPerformed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
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
    private final ApplicationEventPublisher events;

    @ApplicationModuleListener
    public void on(WorkoutPerformed event) {
        List<Referral> referrals = referralRepository.findByReferredEmail(event.userEmail());
        for (Referral referral : referrals) {
            if (!referral.isRewarded()) {
                rewardReferral(referral);
            }
        }
    }

    private void rewardReferral(Referral referral) {
        referral.reward();
        referralRepository.save(referral);
        events.publishEvent(new ReferralRewarded(referral.getReferrerEmail(), referral.getReferredEmail(), referral.getReferralDate()));
    }

    // todo check if users exist / do not exist
    public Referral createReferral(String referrerEmail, ReferralDto referralDto) {
        Referral referral = new Referral(referrerEmail, referralDto.referredEmail(), LocalDateTime.now());
        return referralRepository.save(referral);
    }

    public List<Referral> getReferralsByReferrer(String referrerEmail) {
        return referralRepository.findByReferrerEmail(referrerEmail);
    }
}
