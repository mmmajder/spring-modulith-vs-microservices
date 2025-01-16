package com.majder.giveaway.workout;

import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public record WorkoutPerformed(String userEmail, Long minutes, Long gymId) {
}
