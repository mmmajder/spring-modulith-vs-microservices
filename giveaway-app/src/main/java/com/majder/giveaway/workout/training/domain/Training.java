package com.majder.giveaway.workout.training.domain;

import org.jmolecules.ddd.annotation.AggregateRoot;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jmolecules.ddd.annotation.Identity;

import java.time.Duration;
import java.time.LocalDateTime;

@AggregateRoot
@Entity
@Getter
@NoArgsConstructor
@Table(name = "training", schema = "WORKOUT")
public class Training {
    @Identity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;

    private Long gymId;

    private LocalDateTime checkInTime;

    private LocalDateTime checkOutTime;

    @Version
    private Long version;

    public Training(String email, Long gymId, LocalDateTime checkInTime) {
        this.userEmail = email;
        this.gymId = gymId;
        this.checkInTime = checkInTime;
    }

    public void checkOut(LocalDateTime checkOutTime) {
        if (checkOutTime.isBefore(this.checkInTime)) {
            throw new IllegalArgumentException("Check-out time cannot be before check-in time");
        }
        this.checkOutTime = checkOutTime;
    }

    public boolean isActive() {
        return this.checkOutTime == null;
    }

    /**
     * Calculates the workout length in minutes.
     *
     * @return the duration of the workout in minutes, or throws an exception if the workout is still active.
     */
    public long getWorkoutLengthInMinutes() {
        if (this.checkOutTime == null) {
            throw new IllegalStateException("Cannot calculate workout length for an active training session");
        }
        return Duration.between(this.checkInTime, this.checkOutTime).toMinutes();
    }
}
