package org.example.notification.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    Device findByDeviceId(String deviceId);
    List<Device> findByUserEmail(String email);
}
