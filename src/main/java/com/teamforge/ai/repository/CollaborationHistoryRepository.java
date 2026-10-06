package com.teamforge.ai.repository;

import com.teamforge.ai.model.CollaborationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollaborationHistoryRepository extends JpaRepository<CollaborationHistory, Long> {

    @Query("SELECT c FROM CollaborationHistory c WHERE " +
           "(c.student1Id = :s1 AND c.student2Id = :s2) OR " +
           "(c.student1Id = :s2 AND c.student2Id = :s1)")
    Optional<CollaborationHistory> findBetweenStudents(@Param("s1") Long student1Id, @Param("s2") Long student2Id);

    @Query("SELECT c FROM CollaborationHistory c WHERE c.student1Id = :sId OR c.student2Id = :sId")
    List<CollaborationHistory> findByStudent(@Param("sId") Long studentId);
}
