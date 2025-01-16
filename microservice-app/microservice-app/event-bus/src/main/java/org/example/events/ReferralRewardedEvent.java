package org.example.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReferralRewardedEvent {
    private String referrerEmail;
    private String referredEmail;
    private LocalDateTime timestamp;
}
