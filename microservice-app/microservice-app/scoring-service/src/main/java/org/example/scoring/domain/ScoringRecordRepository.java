package org.example.scoring.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScoringRecordRepository extends JpaRepository<ScoringRecord, Long> {
    @Query("SELECT SUM(sr.points) FROM ScoringRecord sr " +
            "WHERE sr.userEmail = :userEmail AND YEAR(sr.timestamp) = :year AND MONTH(sr.timestamp) = :month")
    Integer findTotalPointsByUserEmailByMonth(String userEmail, int year, int month);

    @Query("SELECT sr.userEmail, SUM(sr.points) FROM ScoringRecord sr " +
            "WHERE YEAR(sr.timestamp) = :year AND MONTH(sr.timestamp) = :month " +
            "GROUP BY sr.userEmail")
    List<Object[]> findTotalPointsByMonth(int year, int month);
}
