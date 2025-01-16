package com.majder.giveaway.scoring;

import org.jmolecules.event.annotation.DomainEvent;

import java.util.List;

@DomainEvent
public record MultipleNotificationEvent(List<SingleNotificationEvent> events) {
}