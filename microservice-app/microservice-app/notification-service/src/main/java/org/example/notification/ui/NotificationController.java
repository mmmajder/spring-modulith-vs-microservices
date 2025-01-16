package org.example.notification.ui;

import lombok.RequiredArgsConstructor;
import org.example.notification.application.CreateDeviceDto;
import org.example.notification.application.NotificationService;
import org.example.notification.domain.Device;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification-service/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping()
    public ResponseEntity<Device> registerDevice(@RequestHeader("X-User-Email") String email, @RequestBody CreateDeviceDto createDeviceDto) {
        Device device = notificationService.registerDevice(createDeviceDto, email);
        return ResponseEntity.ok(device);
    }

    @PostMapping("/send")
    public ResponseEntity<List<Device>> testSendNotification(@RequestHeader("X-User-Email") String email) {
        List<Device> devices = notificationService.testSendNotification(email, "title", "body");
        return ResponseEntity.ok(devices);
    }

}
