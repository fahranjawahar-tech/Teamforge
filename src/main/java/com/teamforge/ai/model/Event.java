package com.teamforge.ai.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, length = 80)
    private String category;

    @Column(name = "event_date", length = 50)
    private String eventDate;

    @Column(name = "academic_year", length = 20)
    private String academicYear = "2025-2026";

    @Column(name = "max_teams")
    private Integer maxTeams = 8;

    @Column(name = "assigned_teams_count")
    private Integer assignedTeamsCount = 0;

    @Column(length = 30)
    private String status = "UPCOMING"; // UPCOMING, ACTIVE, COMPLETED

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Event() {}

    public Event(String title, String description, String category, String eventDate, String academicYear, Integer maxTeams) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.eventDate = eventDate;
        this.academicYear = academicYear != null ? academicYear : "2025-2026";
        this.maxTeams = maxTeams != null ? maxTeams : 8;
        this.assignedTeamsCount = 0;
        this.status = "UPCOMING";
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public Integer getMaxTeams() {
        return maxTeams;
    }

    public void setMaxTeams(Integer maxTeams) {
        this.maxTeams = maxTeams;
    }

    public Integer getAssignedTeamsCount() {
        return assignedTeamsCount;
    }

    public void setAssignedTeamsCount(Integer assignedTeamsCount) {
        this.assignedTeamsCount = assignedTeamsCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
