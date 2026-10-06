package com.teamforge.ai.dto;

import java.util.List;
import java.util.Map;

public class TeamComparisonDTO {

    private List<GeneratedTeamDTO> teams;
    private List<String> metricNames;
    private Map<String, List<Double>> metricsByTeam;
    private String aiRecommendationSummary;

    public TeamComparisonDTO() {}

    public TeamComparisonDTO(List<GeneratedTeamDTO> teams, List<String> metricNames, Map<String, List<Double>> metricsByTeam, String aiRecommendationSummary) {
        this.teams = teams;
        this.metricNames = metricNames;
        this.metricsByTeam = metricsByTeam;
        this.aiRecommendationSummary = aiRecommendationSummary;
    }

    public List<GeneratedTeamDTO> getTeams() {
        return teams;
    }

    public void setTeams(List<GeneratedTeamDTO> teams) {
        this.teams = teams;
    }

    public List<String> getMetricNames() {
        return metricNames;
    }

    public void setMetricNames(List<String> metricNames) {
        this.metricNames = metricNames;
    }

    public Map<String, List<Double>> getMetricsByTeam() {
        return metricsByTeam;
    }

    public void setMetricsByTeam(Map<String, List<Double>> metricsByTeam) {
        this.metricsByTeam = metricsByTeam;
    }

    public String getAiRecommendationSummary() {
        return aiRecommendationSummary;
    }

    public void setAiRecommendationSummary(String aiRecommendationSummary) {
        this.aiRecommendationSummary = aiRecommendationSummary;
    }
}
