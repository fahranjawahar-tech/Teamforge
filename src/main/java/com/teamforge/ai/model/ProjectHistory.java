package com.teamforge.ai.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "project_history")
public class ProjectHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "project_name", nullable = false, length = 120)
    private String projectName;

    @Column(nullable = false, length = 150)
    private String technology;

    @Column(nullable = false, length = 60)
    private String result; // "Completed (Grade A)", "Completed (Grade S)", "Deployed to Production", "In Progress"

    @Column(name = "team_members", length = 255)
    private String teamMembers;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public ProjectHistory() {}

    public ProjectHistory(Long studentId, String projectName, String technology, String result, String teamMembers) {
        this.studentId = studentId;
        this.projectName = projectName;
        this.technology = technology;
        this.result = result;
        this.teamMembers = teamMembers;
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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(String teamMembers) {
        this.teamMembers = teamMembers;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
