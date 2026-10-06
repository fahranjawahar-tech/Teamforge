package com.teamforge.ai.service;

import com.teamforge.ai.model.Event;
import com.teamforge.ai.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Transactional(readOnly = true)
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Transactional
    public Event createEvent(Event event) {
        if (event.getTitle() == null || event.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Event title is required");
        }
        if (event.getCategory() == null || event.getCategory().trim().isEmpty()) {
            event.setCategory("General CSE Hackathon");
        }
        if (event.getStatus() == null) {
            event.setStatus("UPCOMING");
        }
        return eventRepository.save(event);
    }

    @Transactional
    public Event incrementAssignedTeamCount(Long eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event != null) {
            event.setAssignedTeamsCount((event.getAssignedTeamsCount() != null ? event.getAssignedTeamsCount() : 0) + 1);
            return eventRepository.save(event);
        }
        return null;
    }
}
