package com.teamforge.ai.repository;

import com.teamforge.ai.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    Optional<Student> findByRegisterNumber(String registerNumber);
    Optional<Student> findByEmail(String email);
    boolean existsByRegisterNumber(String registerNumber);
    boolean existsByEmail(String email);

    List<Student> findByPreferredRole(String preferredRole);
    List<Student> findByStatus(String status);

    @Query("SELECT s FROM Student s WHERE " +
           "LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(s.registerNumber) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(s.email) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(s.preferredRole) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(s.projectInterests) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Student> searchStudents(@Param("query") String query);

    @Query("SELECT AVG(s.codingSkill) as avgCoding, " +
           "AVG(s.designSkill) as avgDesign, " +
           "AVG(s.communicationSkill) as avgComm, " +
           "AVG(s.documentationSkill) as avgDoc " +
           "FROM Student s")
    Object getAverageSkillsAcrossAll();
}
