package com.majder.giveaway.workout.gym.domain;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GymRepository extends JpaRepository<Gym, Long> {

    @Query("""
           SELECT g FROM Gym g 
           WHERE SQRT(POWER(g.address.latitude - :latitude, 2) + POWER(g.address.longitude - :longitude, 2)) < :radius
           """)
    Page<Gym> findClosestGyms(@Param("latitude") double latitude, @Param("longitude") double longitude, @Param("radius") double radius, Pageable pageable);

    @Query("""
       SELECT g FROM Gym g 
       WHERE LOWER(g.address.city) LIKE LOWER(CONCAT('%', :name, '%'))
          OR LOWER(g.name) LIKE LOWER(CONCAT('%', :name, '%'))
       """)
    Page<Gym> findGymsByCityNameOrGymName(@Param("name") String name, Pageable pageable);
}
