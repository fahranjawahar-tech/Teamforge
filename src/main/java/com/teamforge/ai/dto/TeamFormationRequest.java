package com.teamforge.ai.dto;

import java.util.List;

public class TeamFormationRequest {

    private Integer teamSize = 4;
    private Integer numberOfTeams;
    private String projectCategory = "AI & Machine Learning";
    private List<String> requiredRoles;
    private String skillPriority = "BALANCED"; // "BALANCED", "CODING_HEAVY", "DESIGN_HEAVY", "INNOVATION_RESEARCH"
    private List<Long> selectedStudentIds;
    private String academicYear = "2025-2026";

    public TeamFormationRequest() {}

    public Integer getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(Integer teamSize) {
        this.teamSize = teamSize;
    }

    public Integer getNumberOfTeams() {
        return numberOfTeams;
    }

    public void setNumberOfTeams(Integer numberOfTeams) {
        this.numberOfTeams = numberOfTeams;
    }

    public String getProjectCategory() {
        return projectCategory;
    }

    public void setProjectCategory(String projectCategory) {
        this.projectCategory = projectCategory;
    }

    public List<String> getRequiredRoles() {
        return requiredRoles;
    }

    public void setRequiredRoles(List<String> requiredRoles) {
        this.requiredRoles = requiredRoles;
    }

    public String getSkillPriority() {
        return skillPriority;
    }

    public void setSkillPriority(String skillPriority) {
        this.skillPriority = skillPriority;
    }

    public List<Long> getSelectedStudentIds() {
        return selectedStudentIds;
    }

    public void setSelectedStudentIds(List<Long> selectedStudentIds) {
        this.selectedStudentIds = selectedStudentIds;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }
}
