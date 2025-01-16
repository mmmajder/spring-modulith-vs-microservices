package com.majder.giveaway.notification.application;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.majder.giveaway.notification.domain.Device;
import com.majder.giveaway.notification.domain.DeviceRepository;
import com.majder.giveaway.notification.domain.User;
import com.majder.giveaway.notification.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final FirebaseMessaging firebaseMessaging;

    public User registerUser(String name, String email) {
        User user = new User(name, email);
        return userRepository.save(user);
    }

    public Device registerDevice(CreateDeviceDto createDeviceDto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> registerUser(createDeviceDto.name(), email));

        Device device = new Device(createDeviceDto.deviceId(), user.getEmail());
        return deviceRepository.save(device);
    }

    public List<Device> testSendNotification(String email, String title, String body) {
        List<Device> devices = deviceRepository.findByUserEmail(email).stream().toList();

        for (Device device : devices) {
            String deviceId = device.getDeviceId();
            try {
                sendFcmNotification(deviceId, title, body);
                System.out.println("Notification sent to device: " + deviceId);
            } catch (FirebaseMessagingException e) {
                System.err.println("Failed to send notification to device: " + deviceId + " - " + e.getMessage());
            }
        }

        return null;
    }

    private void sendFcmNotification(String deviceId, String title, String body) throws FirebaseMessagingException {
        // Create notification message
        Message message = Message.builder()
                .setToken(deviceId)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();

        // Send message via FirebaseMessaging
        String response = firebaseMessaging.send(message);
        System.out.println("Notification sent successfully: " + response);
    }
}
