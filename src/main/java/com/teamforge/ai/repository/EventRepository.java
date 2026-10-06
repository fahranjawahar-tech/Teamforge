package com.teamforge.ai.repository;

import com.teamforge.ai.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByStatusOrderByCreatedAtDesc(String status);
}
