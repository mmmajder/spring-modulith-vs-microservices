package org.example.referral.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

import java.time.LocalDateTime;

@AggregateRoot
@Entity
@Getter
@NoArgsConstructor
//@Table(name = "referrals")
@Table(name = "referrals", schema = "REFERRAL")
public class Referral {
    @Identity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String referrerEmail;

    @Column(nullable = false)
    private String referredEmail;

    @Column(nullable = false)
    private LocalDateTime referralDate;

    @Column(nullable = false)
    private boolean rewarded;

    public Referral(String referrerEmail, String referredEmail, LocalDateTime referralDate) {
        this.referrerEmail = referrerEmail;
        this.referredEmail = referredEmail;
        this.referralDate = referralDate;
        this.rewarded = false;
    }

    public void reward() {
        this.rewarded = true;
    }
}

