package com.teamforge.ai.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "team_name", nullable = false, length = 100)
    private String teamName;

    @Column(name = "project_category", nullable = false, length = 80)
    private String projectCategory;

    @Column(name = "success_score", nullable = false)
    private Double successScore;

    @Column(name = "prediction_label", length = 50)
    private String predictionLabel = "High Potential";

    @Column(name = "skill_score")
    private Double skillScore;

    @Column(name = "role_diversity_score")
    private Double roleDiversityScore;

    @Column(name = "experience_score")
    private Double experienceScore;

    @Column(name = "hackathon_performance")
    private Double hackathonPerformance;

    @Column(name = "compatibility_score")
    private Double compatibilityScore;

    @Column(name = "strengths_text", length = 1000)
    private String strengthsText;

    @Column(name = "weaknesses_text", length = 1000)
    private String weaknessesText;

    @Column(name = "academic_year", length = 20)
    private String academicYear = "2025-2026";

    @Column(length = 30)
    private String status = "SAVED"; // "SAVED", "ACTIVE", "COMPLETED"

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<TeamMember> members = new ArrayList<>();

    public Team() {}

    public Team(String teamName, String projectCategory, Double successScore, String predictionLabel,
                Double skillScore, Double roleDiversityScore, Double experienceScore, 
                Double hackathonPerformance, Double compatibilityScore, 
                String strengthsText, String weaknessesText, String academicYear) {
        this.teamName = teamName;
        this.projectCategory = projectCategory;
        this.successScore = successScore;
        this.predictionLabel = predictionLabel;
        this.skillScore = skillScore;
        this.roleDiversityScore = roleDiversityScore;
        this.experienceScore = experienceScore;
        this.hackathonPerformance = hackathonPerformance;
        this.compatibilityScore = compatibilityScore;
        this.strengthsText = strengthsText;
        this.weaknessesText = weaknessesText;
        this.academicYear = academicYear != null ? academicYear : "2025-2026";
        this.status = "SAVED";
        this.createdAt = LocalDateTime.now();
    }

    public void addMember(TeamMember member) {
        members.add(member);
        member.setTeam(this);
    }

    public void removeMember(TeamMember member) {
        members.remove(member);
        member.setTeam(null);
    }

    // Getters and Setters
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

    public Double getSuccessScore() {
        return successScore;
    }

    public void setSuccessScore(Double successScore) {
        this.successScore = successScore;
    }

    public String getPredictionLabel() {
        return predictionLabel;
    }

    public void setPredictionLabel(String predictionLabel) {
        this.predictionLabel = predictionLabel;
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

    public String getStrengthsText() {
        return strengthsText;
    }

    public void setStrengthsText(String strengthsText) {
        this.strengthsText = strengthsText;
    }

    public String getWeaknessesText() {
        return weaknessesText;
    }

    public void setWeaknessesText(String weaknessesText) {
        this.weaknessesText = weaknessesText;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
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

    public List<TeamMember> getMembers() {
        return members;
    }

    public void setMembers(List<TeamMember> members) {
        this.members = members;
    }
}
