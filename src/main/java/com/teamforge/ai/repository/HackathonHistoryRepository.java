package com.teamforge.ai.repository;

import com.teamforge.ai.model.HackathonHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HackathonHistoryRepository extends JpaRepository<HackathonHistory, Long> {
    List<HackathonHistory> findByStudentId(Long studentId);
    List<HackathonHistory> findByStudentIdOrderByYearDesc(Long studentId);
    long countByResultContaining(String resultKeyword);
}
