package com.teamforge.ai.dto;

import java.util.List;
import java.util.Map;

public class MlPredictionResponse {
    private Double successScore;
    private String prediction;
    private Map<String, Double> componentScores;
    private List<String> strengths;
    private List<String> weaknesses;
    private List<String> diagnosticRecommendations;
    private Map<String, Double> featureContributions;
    private Double confidence;

    public MlPredictionResponse() {}

    public Double getSuccessScore() {
        return successScore;
    }

    public void setSuccessScore(Double successScore) {
        this.successScore = successScore;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public Map<String, Double> getComponentScores() {
        return componentScores;
    }

    public void setComponentScores(Map<String, Double> componentScores) {
        this.componentScores = componentScores;
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

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }
}
