package com.teamforge.ai.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneratedTeamDTO {

    private Long id;
    private String teamName;
    private String projectCategory;
    private List<TeamMemberDTO> members = new ArrayList<>();
    
    // Performance and Compatibility metrics (0 - 100%)
    private Double skillScore;
    private Double roleDiversityScore;
    private Double experienceScore;
    private Double hackathonPerformance;
    private Double compatibilityScore;
    private Double predictedSuccessScore;
    private String predictionLabel = "High Potential";

    // Explainable AI & Diagnostics
    private List<String> strengths = new ArrayList<>();
    private List<String> weaknesses = new ArrayList<>();
    private List<String> diagnosticRecommendations = new ArrayList<>();
    private Map<String, Double> featureContributions = new HashMap<>();

    // Averages
    private Double averageCoding;
    private Double averageDesign;
    private Double averageCommunication;
    private Double averageDocumentation;
    private Double averageTeamExperience;

    private String academicYear = "2025-2026";
    private boolean saved = false;

    public GeneratedTeamDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getProjectCategory() {
        return projectCategory;
    }

    public void setProjectCategory(String projectCategory) {
        this.projectCategory = projectCategory;
    }

    public List<TeamMemberDTO> getMembers() {
        return members;
    }

    public void setMembers(List<TeamMemberDTO> members) {
        this.members = members;
    }

    public Double getSkillScore() {
        return skillScore;
    }

    public void setSkillScore(Double skillScore) {
        this.skillScore = skillScore;
    }

    public Double getRoleDiversityScore() {
        return roleDiversityScore;
    }

    public void setRoleDiversityScore(Double roleDiversityScore) {
        this.roleDiversityScore = roleDiversityScore;
    }

    public Double getExperienceScore() {
        return experienceScore;
    }

    public void setExperienceScore(Double experienceScore) {
        this.experienceScore = experienceScore;
    }

    public Double getHackathonPerformance() {
        return hackathonPerformance;
    }

    public void setHackathonPerformance(Double hackathonPerformance) {
        this.hackathonPerformance = hackathonPerformance;
    }

    public Double getCompatibilityScore() {
        return compatibilityScore;
    }

    public void setCompatibilityScore(Double compatibilityScore) {
        this.compatibilityScore = compatibilityScore;
    }

    public Double getPredictedSuccessScore() {
        return predictedSuccessScore;
    }

    public void setPredictedSuccessScore(Double predictedSuccessScore) {
        this.predictedSuccessScore = predictedSuccessScore;
    }

    public String getPredictionLabel() {
        return predictionLabel;
    }

    public void setPredictionLabel(String predictionLabel) {
        this.predictionLabel = predictionLabel;
    }

    public List<String> getStrengths() {
        return strengths;
    }

    public void setStrengths(List<String> strengths) {
        this.strengths = strengths;
    }

    public List<String> getWeaknesses() {
        return weaknesses;
    }

    public void setWeaknesses(List<String> weaknesses) {
        this.weaknesses = weaknesses;
    }

    public List<String> getDiagnosticRecommendations() {
        return diagnosticRecommendations;
    }

    public void setDiagnosticRecommendations(List<String> diagnosticRecommendations) {
        this.diagnosticRecommendations = diagnosticRecommendations;
    }

    public Map<String, Double> getFeatureContributions() {
        return featureContributions;
    }

    public void setFeatureContributions(Map<String, Double> featureContributions) {
        this.featureContributions = featureContributions;
    }

    public Double getAverageCoding() {
        return averageCoding;
    }

    public void setAverageCoding(Double averageCoding) {
        this.averageCoding = averageCoding;
    }

    public Double getAverageDesign() {
        return averageDesign;
    }

    public void setAverageDesign(Double averageDesign) {
        this.averageDesign = averageDesign;
    }

    public Double getAverageCommunication() {
        return averageCommunication;
    }

    public void setAverageCommunication(Double averageCommunication) {
        this.averageCommunication = averageCommunication;
    }

    public Double getAverageDocumentation() {
        return averageDocumentation;
    }

    public void setAverageDocumentation(Double averageDocumentation) {
        this.averageDocumentation = averageDocumentation;
    }

    public Double getAverageTeamExperience() {
        return averageTeamExperience;
    }

    public void setAverageTeamExperience(Double averageTeamExperience) {
        this.averageTeamExperience = averageTeamExperience;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }
}
