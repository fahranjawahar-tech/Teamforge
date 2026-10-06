package com.teamforge.ai.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hackathon_history")
public class HackathonHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "hackathon_name", nullable = false, length = 120)
    private String hackathonName;

    @Column(name = "event_year", nullable = false)
    private Integer year;

    @Column(nullable = false, length = 60)
    private String result; // "1st Place (Winner)", "2nd Runner Up", "Finalist", "Special Mention", "Participant"

    @Column(name = "team_name", length = 80)
    private String teamName;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public HackathonHistory() {}

    public HackathonHistory(Long studentId, String hackathonName, Integer year, String result, String teamName) {
        this.studentId = studentId;
        this.hackathonName = hackathonName;
        this.year = year;
        this.result = result;
        this.teamName = teamName;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getHackathonName() {
        return hackathonName;
    }

    public void setHackathonName(String hackathonName) {
        this.hackathonName = hackathonName;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
