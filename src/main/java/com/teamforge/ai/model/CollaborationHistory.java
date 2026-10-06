package com.teamforge.ai.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "collaboration_history")
public class CollaborationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student1_id", nullable = false)
    private Long student1Id;

    @Column(name = "student2_id", nullable = false)
    private Long student2Id;

    @Column(name = "student1_name", length = 100)
    private String student1Name;

    @Column(name = "student2_name", length = 100)
    private String student2Name;

    @Column(name = "projects_together", nullable = false)
    private Integer projectsTogether = 1;

    @Column(name = "completed_projects", nullable = false)
    private Integer completedProjects = 1;

    @Column(name = "collaboration_score", nullable = false)
    private Double collaborationScore = 85.0; // 0 - 100%

    @Column(name = "last_project_name", length = 150)
    private String lastProjectName;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public CollaborationHistory() {}

    public CollaborationHistory(Long student1Id, Long student2Id, String student1Name, String student2Name,
                                Integer projectsTogether, Integer completedProjects, Double collaborationScore,
                                String lastProjectName) {
        this.student1Id = student1Id;
        this.student2Id = student2Id;
        this.student1Name = student1Name;
        this.student2Name = student2Name;
        this.projectsTogether = projectsTogether;
        this.completedProjects = completedProjects;
        this.collaborationScore = collaborationScore;
        this.lastProjectName = lastProjectName;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudent1Id() {
        return student1Id;
    }

    public void setStudent1Id(Long student1Id) {
        this.student1Id = student1Id;
    }

    public Long getStudent2Id() {
        return student2Id;
    }

    public void setStudent2Id(Long student2Id) {
        this.student2Id = student2Id;
    }

    public String getStudent1Name() {
        return student1Name;
    }

    public void setStudent1Name(String student1Name) {
        this.student1Name = student1Name;
    }

    public String getStudent2Name() {
        return student2Name;
    }

    public void setStudent2Name(String student2Name) {
        this.student2Name = student2Name;
    }

    public Integer getProjectsTogether() {
        return projectsTogether;
    }

    public void setProjectsTogether(Integer projectsTogether) {
        this.projectsTogether = projectsTogether;
    }

    public Integer getCompletedProjects() {
        return completedProjects;
    }

    public void setCompletedProjects(Integer completedProjects) {
        this.completedProjects = completedProjects;
    }

    public Double getCollaborationScore() {
        return collaborationScore;
    }

    public void setCollaborationScore(Double collaborationScore) {
        this.collaborationScore = collaborationScore;
    }

    public String getLastProjectName() {
        return lastProjectName;
    }

    public void setLastProjectName(String lastProjectName) {
        this.lastProjectName = lastProjectName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
