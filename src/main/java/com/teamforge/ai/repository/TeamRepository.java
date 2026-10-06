package com.teamforge.ai.repository;

import com.teamforge.ai.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findAllByOrderBySuccessScoreDesc();

    List<Team> findByProjectCategoryIgnoreCaseOrderBySuccessScoreDesc(String projectCategory);

    List<Team> findByAcademicYearOrderBySuccessScoreDesc(String academicYear);

    Optional<Team> findTopByOrderBySuccessScoreDesc();

    @Query("SELECT AVG(t.successScore) FROM Team t")
    Double getAverageSuccessScore();

    @Query("SELECT COUNT(t) FROM Team t WHERE t.successScore >= 80.0")
    long countHighPotentialTeams();
}
