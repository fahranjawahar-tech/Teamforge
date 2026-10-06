package com.teamforge.ai.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Student name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Register number is required")
    @Column(name = "register_number", nullable = false, unique = true, length = 30)
    private String registerNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Min(value = 1, message = "Coding skill must be between 1 and 5")
    @Max(value = 5, message = "Coding skill must be between 1 and 5")
    @Column(name = "coding_skill", nullable = false)
    private Integer codingSkill = 3;

    @Min(value = 1, message = "Design skill must be between 1 and 5")
    @Max(value = 5, message = "Design skill must be between 1 and 5")
    @Column(name = "design_skill", nullable = false)
    private Integer designSkill = 3;

    @Min(value = 1, message = "Communication skill must be between 1 and 5")
    @Max(value = 5, message = "Communication skill must be between 1 and 5")
    @Column(name = "communication_skill", nullable = false)
    private Integer communicationSkill = 3;

    @Min(value = 1, message = "Documentation skill must be between 1 and 5")
    @Max(value = 5, message = "Documentation skill must be between 1 and 5")
    @Column(name = "documentation_skill", nullable = false)
    private Integer documentationSkill = 3;

    @NotBlank(message = "Preferred role is required")
    @Column(name = "preferred_role", nullable = false, length = 60)
    private String preferredRole = "Developer";

    @Column(name = "project_interests", length = 255)
    private String projectInterests = "Web Development, AI/ML";

    @Min(value = 0, message = "Hackathons participated cannot be negative")
    @Column(name = "hackathons_participated", nullable = false)
    private Integer hackathonsParticipated = 0;

    @Min(value = 0, message = "Hackathons won cannot be negative")
    @Column(name = "hackathons_won", nullable = false)
    private Integer hackathonsWon = 0;

    @Min(value = 0, message = "Previous projects cannot be negative")
    @Column(name = "previous_projects", nullable = false)
    private Integer previousProjects = 0;

    @Min(value = 0, message = "Projects completed cannot be negative")
    @Column(name = "projects_completed", nullable = false)
    private Integer projectsCompleted = 0;

    @Min(value = 1, message = "Team experience must be between 1 and 5")
    @Max(value = 5, message = "Team experience must be between 1 and 5")
    @Column(name = "team_experience", nullable = false)
    private Integer teamExperience = 3;

    @Column(length = 20)
    private String status = "AVAILABLE"; // "AVAILABLE", "ASSIGNED", "INACTIVE"

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Student() {}

    public Student(String name, String registerNumber, String email, 
                   Integer codingSkill, Integer designSkill, Integer communicationSkill, Integer documentationSkill,
                   String preferredRole, String projectInterests, 
                   Integer hackathonsParticipated, Integer hackathonsWon, 
                   Integer previousProjects, Integer projectsCompleted, Integer teamExperience) {
        this.name = name;
        this.registerNumber = registerNumber;
        this.email = email;
        this.codingSkill = codingSkill;
        this.designSkill = designSkill;
        this.communicationSkill = communicationSkill;
        this.documentationSkill = documentationSkill;
        this.preferredRole = preferredRole;
        this.projectInterests = projectInterests;
        this.hackathonsParticipated = hackathonsParticipated != null ? hackathonsParticipated : 0;
        this.hackathonsWon = hackathonsWon != null ? hackathonsWon : 0;
        this.previousProjects = previousProjects != null ? previousProjects : 0;
        this.projectsCompleted = projectsCompleted != null ? projectsCompleted : 0;
        this.teamExperience = teamExperience != null ? teamExperience : 3;
        this.status = "AVAILABLE";
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Calculated Hackathon Win Rate percentage.
     * Prevents division by zero.
     */
    @Transient
    public Double getHackathonWinRate() {
        if (hackathonsParticipated == null || hackathonsParticipated == 0) {
            return 0.0;
        }
        return Math.round(((double) hackathonsWon / hackathonsParticipated) * 1000.0) / 10.0;
    }

    /**
     * Win Rate display label.
     */
    @Transient
    public String getWinRateDisplay() {
        if (hackathonsParticipated == null || hackathonsParticipated == 0) {
            return "No Hackathon History";
        }
        return getHackathonWinRate() + "%";
    }

    /**
     * Calculated Project Completion Rate.
     */
    @Transient
    public Double getProjectCompletionRate() {
        if (previousProjects == null || previousProjects == 0) {
            return 100.0;
        }
        return Math.round(((double) projectsCompleted / previousProjects) * 1000.0) / 10.0;
    }

    /**
     * Overall average skill rating (1-5 scale).
     */
    @Transient
    public Double getAverageSkill() {
        double total = (codingSkill != null ? codingSkill : 0) +
                       (designSkill != null ? designSkill : 0) +
                       (communicationSkill != null ? communicationSkill : 0) +
                       (documentationSkill != null ? documentationSkill : 0);
        return Math.round((total / 4.0) * 10.0) / 10.0;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCodingSkill() {
        return codingSkill;
    }

    public void setCodingSkill(Integer codingSkill) {
        this.codingSkill = codingSkill;
    }

    public Integer getDesignSkill() {
        return designSkill;
    }

    public void setDesignSkill(Integer designSkill) {
        this.designSkill = designSkill;
    }

    public Integer getCommunicationSkill() {
        return communicationSkill;
    }

    public void setCommunicationSkill(Integer communicationSkill) {
        this.communicationSkill = communicationSkill;
    }

    public Integer getDocumentationSkill() {
        return documentationSkill;
    }

    public void setDocumentationSkill(Integer documentationSkill) {
        this.documentationSkill = documentationSkill;
    }

    public String getPreferredRole() {
        return preferredRole;
    }

    public void setPreferredRole(String preferredRole) {
        this.preferredRole = preferredRole;
    }

    public String getProjectInterests() {
        return projectInterests;
    }

    public void setProjectInterests(String projectInterests) {
        this.projectInterests = projectInterests;
    }

    public Integer getHackathonsParticipated() {
        return hackathonsParticipated;
    }

    public void setHackathonsParticipated(Integer hackathonsParticipated) {
        this.hackathonsParticipated = hackathonsParticipated;
    }

    public Integer getHackathonsWon() {
        return hackathonsWon;
    }

    public void setHackathonsWon(Integer hackathonsWon) {
        this.hackathonsWon = hackathonsWon;
    }

    public Integer getPreviousProjects() {
        return previousProjects;
    }

    public void setPreviousProjects(Integer previousProjects) {
        this.previousProjects = previousProjects;
    }

    public Integer getProjectsCompleted() {
        return projectsCompleted;
    }

    public void setProjectsCompleted(Integer projectsCompleted) {
        this.projectsCompleted = projectsCompleted;
    }

    public Integer getTeamExperience() {
        return teamExperience;
    }

    public void setTeamExperience(Integer teamExperience) {
        this.teamExperience = teamExperience;
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
