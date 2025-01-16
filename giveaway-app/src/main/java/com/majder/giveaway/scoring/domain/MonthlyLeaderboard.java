package com.majder.giveaway.scoring.domain;


import jakarta.persistence.*;
import org.jmolecules.ddd.annotation.AggregateRoot;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jmolecules.ddd.annotation.Identity;
import org.jmolecules.ddd.annotation.ValueObject;

import java.util.List;

@AggregateRoot
@Entity
@Getter
@NoArgsConstructor
@Table(name = "monthly_leaderboard", schema = "SCORING")
public class MonthlyLeaderboard {
    @Identity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String leaderboardMonth;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "MONTHLY_LEADERBOARD_WINNERS",
            schema = "SCORING",
            joinColumns = @JoinColumn(name = "MONTHLY_LEADERBOARD_ID")
    )
    private List<Winner> winners;

    public MonthlyLeaderboard(String month, List<Winner> winners) {
        this.leaderboardMonth = month;
        this.winners = winners;
    }

    public void setTopUsers(List<Winner> topUsers) {
        this.winners = topUsers;
    }

    @ValueObject
    @Embeddable
    public record Winner(String email, int points){}
}
