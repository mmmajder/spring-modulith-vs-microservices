package com.majder.giveaway.referral.ui;

import com.majder.giveaway.referral.application.ReferralDto;
import com.majder.giveaway.referral.application.ReferralService;
import com.majder.giveaway.referral.domain.Referral;
import com.majder.giveaway.useraccount.UserAccount;
import com.majder.giveaway.useraccount.web.Authenticated;
import com.majder.giveaway.utils.annotation.Log;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/referrals")
@RequiredArgsConstructor
public class ReferralController {
    private final ReferralService referralService;

    @PostMapping
    @Log(message = "Creating a new referral")
    public ResponseEntity<Referral> createReferral(@Authenticated UserAccount userAccount, @RequestBody ReferralDto referralDto) {
        Referral referral = referralService.createReferral(userAccount.email(), referralDto);
        return ResponseEntity.ok(referral);
    }

    @GetMapping
    @Log(message = "Fetching referrals by referrer")
    public ResponseEntity<List<Referral>> getReferralsByReferrer(@Authenticated UserAccount userAccount) {
        List<Referral> referrals = referralService.getReferralsByReferrer(userAccount.email());
        return ResponseEntity.ok(referrals);
    }
}
