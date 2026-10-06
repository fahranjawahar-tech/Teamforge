package com.teamforge.ai.dto;

public class TeamMemberDTO {
    private Long studentId;
    private String name;
    private String registerNumber;
    private String email;
    private String preferredRole;
    private String assignedRole;
    private Integer codingSkill;
    private Integer designSkill;
    private Integer communicationSkill;
    private Integer documentationSkill;
    private Double hackathonWinRate;
    private String winRateDisplay;
    private Integer teamExperience;

    public TeamMemberDTO() {}

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
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

    public String getPreferredRole() {
        return preferredRole;
    }

    public void setPreferredRole(String preferredRole) {
        this.preferredRole = preferredRole;
    }

    public String getAssignedRole() {
        return assignedRole;
    }

    public void setAssignedRole(String assignedRole) {
        this.assignedRole = assignedRole;
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

    public Integer getTeamExperience() {
        return teamExperience;
    }

    public void setTeamExperience(Integer teamExperience) {
        this.teamExperience = teamExperience;
    }
}
