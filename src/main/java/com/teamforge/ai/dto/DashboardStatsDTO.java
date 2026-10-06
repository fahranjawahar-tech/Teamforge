package com.teamforge.ai.dto;

import java.util.Map;

public class DashboardStatsDTO {

    private long totalStudents;
    private long totalTeams;
    private long totalHackathons;
    private long totalProjects;
    private Double averageTeamSuccessScore;
    private String topPerformingTeamName;
    private Double topPerformingTeamScore;
    private String topPerformingCategory;
    private long highPotentialTeamsCount;

    // Charts data
    private Map<String, Double> skillAverages; // Coding, Design, Communication, Documentation
    private Map<String, Long> roleDistribution;
    private Map<String, Long> hackathonStats; // Participated, Won
    private Map<String, Long> projectCategoryCounts;

    public DashboardStatsDTO() {}

    public long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public long getTotalTeams() {
        return totalTeams;
    }

    public void setTotalTeams(long totalTeams) {
        this.totalTeams = totalTeams;
    }

    public long getTotalHackathons() {
        return totalHackathons;
    }

    public void setTotalHackathons(long totalHackathons) {
        this.totalHackathons = totalHackathons;
    }

    public long getTotalProjects() {
        return totalProjects;
    }

    public void setTotalProjects(long totalProjects) {
        this.totalProjects = totalProjects;
    }

    public Double getAverageTeamSuccessScore() {
        return averageTeamSuccessScore;
    }

    public void setAverageTeamSuccessScore(Double averageTeamSuccessScore) {
        this.averageTeamSuccessScore = averageTeamSuccessScore;
    }

    public String getTopPerformingTeamName() {
        return topPerformingTeamName;
    }

    public void setTopPerformingTeamName(String topPerformingTeamName) {
        this.topPerformingTeamName = topPerformingTeamName;
    }

    public Double getTopPerformingTeamScore() {
        return topPerformingTeamScore;
    }

    public void setTopPerformingTeamScore(Double topPerformingTeamScore) {
        this.topPerformingTeamScore = topPerformingTeamScore;
    }

    public String getTopPerformingCategory() {
        return topPerformingCategory;
    }

    public void setTopPerformingCategory(String topPerformingCategory) {
        this.topPerformingCategory = topPerformingCategory;
    }

    public long getHighPotentialTeamsCount() {
        return highPotentialTeamsCount;
    }

    public void setHighPotentialTeamsCount(long highPotentialTeamsCount) {
        this.highPotentialTeamsCount = highPotentialTeamsCount;
    }

    public Map<String, Double> getSkillAverages() {
        return skillAverages;
    }

    public void setSkillAverages(Map<String, Double> skillAverages) {
        this.skillAverages = skillAverages;
    }

    public Map<String, Long> getRoleDistribution() {
        return roleDistribution;
    }

    public void setRoleDistribution(Map<String, Long> roleDistribution) {
        this.roleDistribution = roleDistribution;
    }

    public Map<String, Long> getHackathonStats() {
        return hackathonStats;
    }

    public void setHackathonStats(Map<String, Long> hackathonStats) {
        this.hackathonStats = hackathonStats;
    }

    public Map<String, Long> getProjectCategoryCounts() {
        return projectCategoryCounts;
    }

    public void setProjectCategoryCounts(Map<String, Long> projectCategoryCounts) {
        this.projectCategoryCounts = projectCategoryCounts;
    }
}
