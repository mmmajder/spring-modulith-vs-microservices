package com.majder.giveaway.notification.application;

import com.majder.giveaway.notification.domain.Device;
import com.majder.giveaway.notification.domain.DeviceRepository;
import com.majder.giveaway.referral.ReferralRewarded;
import com.majder.giveaway.scoring.MultipleNotificationEvent;
import com.majder.giveaway.scoring.SingleNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
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

    @ApplicationModuleListener
    public void on(MultipleNotificationEvent multipleEvents) {
        for (SingleNotificationEvent event : multipleEvents.events()) {
            sendNotification(event.userEmail(), event.title(), event.body());
        }
    }

    @ApplicationModuleListener
    public void on(ReferralRewarded event) {
        sendNotification(event.referrerEmail(), "Successful referral", "Congrats! Your referral just completed their first workout. You've earned new points—check them out now!");
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
