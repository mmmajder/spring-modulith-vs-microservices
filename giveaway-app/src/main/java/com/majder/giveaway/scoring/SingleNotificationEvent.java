package com.majder.giveaway.scoring;

public record SingleNotificationEvent(String userEmail, String title, String body) {}
