package com.teamforge.ai.dto;

import java.time.LocalDateTime;
import java.util.List;

public class StudentDTO {
    private Long id;
    private String name;
    private String registerNumber;
    private String email;
    private Integer codingSkill;
    private Integer designSkill;
    private Integer communicationSkill;
    private Integer documentationSkill;
    private String preferredRole;
    private String projectInterests;
    private Integer hackathonsParticipated;
    private Integer hackathonsWon;
    private Integer previousProjects;
    private Integer projectsCompleted;
    private Integer teamExperience;
    private String status;
    private Double hackathonWinRate;
    private String winRateDisplay;
    private Double projectCompletionRate;
    private Double averageSkill;
    private LocalDateTime createdAt;

    private List<HackathonHistoryDTO> hackathonHistory;
    private List<ProjectHistoryDTO> projectHistory;

    public StudentDTO() {}

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

    public Double getHackathonWinRate() {
        return hackathonWinRate;
    }

    public void setHackathonWinRate(Double hackathonWinRate) {
        this.hackathonWinRate = hackathonWinRate;
    }

    public String getWinRateDisplay() {
        return winRateDisplay;
    }

    public void setWinRateDisplay(String winRateDisplay) {
        this.winRateDisplay = winRateDisplay;
    }

    public Double getProjectCompletionRate() {
        return projectCompletionRate;
    }

    public void setProjectCompletionRate(Double projectCompletionRate) {
        this.projectCompletionRate = projectCompletionRate;
    }

    public Double getAverageSkill() {
        return averageSkill;
    }

    public void setAverageSkill(Double averageSkill) {
        this.averageSkill = averageSkill;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<HackathonHistoryDTO> getHackathonHistory() {
        return hackathonHistory;
    }

    public void setHackathonHistory(List<HackathonHistoryDTO> hackathonHistory) {
        this.hackathonHistory = hackathonHistory;
    }

    public List<ProjectHistoryDTO> getProjectHistory() {
        return projectHistory;
    }

    public void setProjectHistory(List<ProjectHistoryDTO> projectHistory) {
        this.projectHistory = projectHistory;
    }
}
