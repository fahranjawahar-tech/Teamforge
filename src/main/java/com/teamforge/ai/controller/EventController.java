package com.teamforge.ai.controller;

import com.teamforge.ai.model.Event;
import com.teamforge.ai.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event created = eventService.createEvent(event);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/assign-team")
    public ResponseEntity<Event> assignTeam(@PathVariable Long id) {
        Event updated = eventService.incrementAssignedTeamCount(id);
        return ResponseEntity.ok(updated);
    }
}
