package com.majder.giveaway.workout.gym.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;
import org.jmolecules.ddd.annotation.ValueObject;

@AggregateRoot
@Entity
@Getter
@NoArgsConstructor
//@Table(name = "gyms")
@Table(name = "gyms", schema = "WORKOUT")
public class Gym {
    @Identity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Address address;

    private int numberOfVisits;

    @Version
    private Long version;

    public Gym(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public void increaseNumberOfVisits() {
        numberOfVisits += 1;
    }

    @ValueObject
    @Embeddable
    public record Address(
            @Column(name = "address_street") String street,
            @Column(name = "address_city") String city,
            @Column(name = "address_country") String country,
            @Column(name = "address_number") int number,
            @Column(name = "address_latitude") double latitude,
            @Column(name = "address_longitude") double longitude
    ) {
    }
}
