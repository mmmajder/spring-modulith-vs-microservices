package org.example.notification.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.events.MultipleNotificationEvent;
import org.example.events.ReferralRewardedEvent;
import org.example.events.SingleNotificationEvent;
import org.example.events.WorkoutPerformedEvent;
import org.example.notification.domain.Device;
import org.example.notification.domain.DeviceRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class SendNotificationService {
    private final DeviceRepository deviceRepository;
    private final NotificationService notificationService;

    @KafkaListener(topics = "leaderboard-update")
    public void on(MultipleNotificationEvent multipleEvents) {
        for (SingleNotificationEvent event : multipleEvents.getEvents()) {
            sendNotification(event.getUserEmail(), event.getTitle(), event.getBody());
        }
    }

    @KafkaListener(topics = "referral-rewarded")
    public void on(ReferralRewardedEvent event) {
        sendNotification(event.getReferrerEmail(), "Successful referral", "Congrats! Your referral just completed their first workout. You've earned new points—check them out now!");
    }

    public void sendNotification(String userEmail, String title, String body) {
        List<Device> devices = deviceRepository.findByUserEmail(userEmail);
        if (devices.isEmpty()) {
            throw new RuntimeException("Device not found");
        }

        // Simulated FCM send logic
        for (Device device : devices) {
            notificationService.testSendNotification(device.getUserEmail(), title, body);
            System.out.printf("Sending notification to device %s: [%s] %s%n", device.getDeviceId(), title, body);
        }
        // Replace with actual FCM logic here (e.g., using Firebase Admin SDK)
    }
}
