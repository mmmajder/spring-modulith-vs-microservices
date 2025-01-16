package com.majder.giveaway.notification.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

@AggregateRoot
@Entity
@Getter
@NoArgsConstructor
@Table(name = "devices", schema = "NOTIFICATION")
public class Device {
    @Identity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String deviceId;

    // Instead of a direct reference to User, we store the userId as an identifier.
    @Column(nullable = false)
    private String userEmail;

    public Device(String deviceId, String userEmail) {
        this.deviceId = deviceId;
        this.userEmail = userEmail;  // Set the userId, not the User object.
    }
}
