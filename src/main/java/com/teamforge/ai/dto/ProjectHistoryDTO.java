package com.teamforge.ai.dto;

public class ProjectHistoryDTO {
    private Long id;
    private Long studentId;
    private String projectName;
    private String technology;
    private String result;
    private String teamMembers;

    public ProjectHistoryDTO() {}

    public ProjectHistoryDTO(Long id, Long studentId, String projectName, String technology, String result, String teamMembers) {
        this.id = id;
        this.studentId = studentId;
        this.projectName = projectName;
        this.technology = technology;
        this.result = result;
        this.teamMembers = teamMembers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(String teamMembers) {
        this.teamMembers = teamMembers;
    }
}
