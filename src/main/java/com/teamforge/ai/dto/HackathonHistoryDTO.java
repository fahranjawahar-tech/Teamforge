package com.teamforge.ai.dto;

public class HackathonHistoryDTO {
    private Long id;
    private Long studentId;
    private String hackathonName;
    private Integer year;
    private String result;
    private String teamName;

    public HackathonHistoryDTO() {}

    public HackathonHistoryDTO(Long id, Long studentId, String hackathonName, Integer year, String result, String teamName) {
        this.id = id;
        this.studentId = studentId;
        this.hackathonName = hackathonName;
        this.year = year;
        this.result = result;
        this.teamName = teamName;
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

    public String getHackathonName() {
        return hackathonName;
    }

    public void setHackathonName(String hackathonName) {
        this.hackathonName = hackathonName;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
