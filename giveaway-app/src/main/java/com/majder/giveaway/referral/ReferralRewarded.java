package com.majder.giveaway.referral;

import org.jmolecules.event.annotation.DomainEvent;

import java.time.LocalDateTime;

@DomainEvent
public record ReferralRewarded(String referrerEmail, String referredEmail, LocalDateTime timestamp) {
}
