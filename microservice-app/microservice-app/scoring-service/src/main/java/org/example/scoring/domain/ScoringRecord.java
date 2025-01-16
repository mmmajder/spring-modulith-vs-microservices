package org.example.scoring.domain;

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
@Table(name = "scoring_record", schema = "SCORING")
public class ScoringRecord {
    @Identity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;
    private int points;
    private String description;
    private LocalDateTime timestamp;

    public ScoringRecord(String userEmail, int points, String description, LocalDateTime timestamp) {
        this.userEmail = userEmail;
        this.points = points;
        this.description = description;
        this.timestamp = timestamp;
    }
}
