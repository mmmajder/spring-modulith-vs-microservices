package com.majder.giveaway.referral.domain;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReferralRepository extends JpaRepository<Referral, Long> {
    List<Referral> findByReferrerEmail(String referrerEmail);
    List<Referral> findByReferredEmail(String referredEmail);
}
