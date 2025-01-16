package org.example.referral.ui;

import lombok.RequiredArgsConstructor;
import org.example.referral.application.ReferralDto;
import org.example.referral.application.ReferralService;
import org.example.referral.domain.Referral;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/referral-service/referrals")
@RequiredArgsConstructor
public class ReferralController {
    private final ReferralService referralService;

    @PostMapping
    public ResponseEntity<Referral> createReferral(@RequestHeader("X-User-Email") String email, @RequestBody ReferralDto referralDto) {
        return ResponseEntity.ok(referralService.createReferral(email, referralDto));
    }

    @GetMapping
    public ResponseEntity<List<Referral>> getReferralsByReferrer(@RequestHeader("X-User-Email") String email) {
        return ResponseEntity.ok(referralService.getReferralsByReferrer(email));
    }
}
