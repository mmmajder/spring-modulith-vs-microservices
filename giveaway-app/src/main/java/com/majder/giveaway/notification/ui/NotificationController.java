package com.majder.giveaway.notification.ui;

import com.majder.giveaway.notification.application.CreateDeviceDto;
import com.majder.giveaway.notification.application.NotificationService;
import com.majder.giveaway.notification.domain.Device;
import com.majder.giveaway.useraccount.UserAccount;
import com.majder.giveaway.useraccount.web.Authenticated;
import com.majder.giveaway.utils.annotation.Log;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping()
    @Log(message = "Registering device for user")
    public ResponseEntity<Device> registerDevice(@Authenticated UserAccount userAccount, @RequestBody CreateDeviceDto createDeviceDto) {
        Device device = notificationService.registerDevice(createDeviceDto, userAccount.email());
        return ResponseEntity.ok(device);
    }

    @PostMapping("/send")
    @Log(message = "Sending test notification to user")
    public ResponseEntity<List<Device>> testSendNotification(@Authenticated UserAccount userAccount) {
        List<Device> devices = notificationService.testSendNotification(userAccount.email(), "title", "body");
        return ResponseEntity.ok(devices);
    }
}
