package org.example.training.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TrainingRepository extends JpaRepository<Training, Long> {
    @Query("SELECT t FROM Training t WHERE t.userEmail = :email AND t.checkOutTime IS NULL")
    List<Training> findActiveTrainingsByUser(@Param("email") String email);

    @Query("SELECT t FROM Training t WHERE t.userEmail = :userEmail AND t.gymId = :gymId AND t.checkOutTime IS NULL")
    Optional<Training> findActiveTrainingByUserAndGym(@Param("userEmail") String userEmail, @Param("gymId") Long gymId);
}
