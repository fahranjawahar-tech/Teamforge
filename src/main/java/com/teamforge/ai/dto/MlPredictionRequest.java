package com.teamforge.ai.dto;

public class MlPredictionRequest {
    private Double averageCoding;
    private Double averageDesign;
    private Double averageCommunication;
    private Double averageDocumentation;
    private Double roleDiversity;
    private Double interestSimilarity;
    private Double collaborationScore;
    private Double teamExperience;
    private Double hackathonWinRate;
    private Double projectCompletionRate;

    public MlPredictionRequest() {}

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

    public Double getRoleDiversity() {
        return roleDiversity;
    }

    public void setRoleDiversity(Double roleDiversity) {
        this.roleDiversity = roleDiversity;
    }

    public Double getInterestSimilarity() {
        return interestSimilarity;
    }

    public void setInterestSimilarity(Double interestSimilarity) {
        this.interestSimilarity = interestSimilarity;
    }

    public Double getCollaborationScore() {
        return collaborationScore;
    }

    public void setCollaborationScore(Double collaborationScore) {
        this.collaborationScore = collaborationScore;
    }

    public Double getTeamExperience() {
        return teamExperience;
    }

    public void setTeamExperience(Double teamExperience) {
        this.teamExperience = teamExperience;
    }

    public Double getHackathonWinRate() {
        return hackathonWinRate;
    }

    public void setHackathonWinRate(Double hackathonWinRate) {
        this.hackathonWinRate = hackathonWinRate;
    }

    public Double getProjectCompletionRate() {
        return projectCompletionRate;
    }

    public void setProjectCompletionRate(Double projectCompletionRate) {
        this.projectCompletionRate = projectCompletionRate;
    }
}
