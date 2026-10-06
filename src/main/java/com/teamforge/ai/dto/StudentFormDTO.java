package com.teamforge.ai.dto;

import jakarta.validation.constraints.*;

public class StudentFormDTO {

    @NotBlank(message = "Student name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Register number is required")
    @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "Register number must contain only letters, numbers, and hyphens")
    private String registerNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotNull(message = "Coding skill is required")
    @Min(value = 1, message = "Coding skill must be between 1 and 5")
    @Max(value = 5, message = "Coding skill must be between 1 and 5")
    private Integer codingSkill;

    @NotNull(message = "Design skill is required")
    @Min(value = 1, message = "Design skill must be between 1 and 5")
    @Max(value = 5, message = "Design skill must be between 1 and 5")
    private Integer designSkill;

    @NotNull(message = "Communication skill is required")
    @Min(value = 1, message = "Communication skill must be between 1 and 5")
    @Max(value = 5, message = "Communication skill must be between 1 and 5")
    private Integer communicationSkill;

    @NotNull(message = "Documentation skill is required")
    @Min(value = 1, message = "Documentation skill must be between 1 and 5")
    @Max(value = 5, message = "Documentation skill must be between 1 and 5")
    private Integer documentationSkill;

    @NotBlank(message = "Preferred role is required")
    private String preferredRole;

    private String projectInterests;

    @Min(value = 0, message = "Hackathons participated cannot be negative")
    private Integer hackathonsParticipated = 0;

    @Min(value = 0, message = "Hackathons won cannot be negative")
    private Integer hackathonsWon = 0;

    @Min(value = 0, message = "Previous projects cannot be negative")
    private Integer previousProjects = 0;

    @Min(value = 0, message = "Projects completed cannot be negative")
    private Integer projectsCompleted = 0;

    @NotNull(message = "Team experience is required")
    @Min(value = 1, message = "Team experience must be between 1 and 5")
    @Max(value = 5, message = "Team experience must be between 1 and 5")
    private Integer teamExperience = 3;

    public StudentFormDTO() {}

    public void validateBusinessRules() {
        if (hackathonsWon != null && hackathonsParticipated != null && hackathonsWon > hackathonsParticipated) {
            throw new IllegalArgumentException("Hackathons won (" + hackathonsWon + ") cannot be greater than hackathons participated (" + hackathonsParticipated + ")");
        }
        if (projectsCompleted != null && previousProjects != null && projectsCompleted > previousProjects) {
            throw new IllegalArgumentException("Projects completed (" + projectsCompleted + ") cannot be greater than previous projects (" + previousProjects + ")");
        }
    }

    // Getters and Setters
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
}
