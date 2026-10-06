package com.teamforge.ai.dto;

public class CollaborationDTO {
    private Long id;
    private Long student1Id;
    private Long student2Id;
    private String student1Name;
    private String student2Name;
    private Integer projectsTogether;
    private Integer completedProjects;
    private Double collaborationScore;
    private String lastProjectName;

    public CollaborationDTO() {}

    public CollaborationDTO(Long id, Long student1Id, Long student2Id, String student1Name, String student2Name,
                            Integer projectsTogether, Integer completedProjects, Double collaborationScore,
                            String lastProjectName) {
        this.id = id;
        this.student1Id = student1Id;
        this.student2Id = student2Id;
        this.student1Name = student1Name;
        this.student2Name = student2Name;
        this.projectsTogether = projectsTogether;
        this.completedProjects = completedProjects;
        this.collaborationScore = collaborationScore;
        this.lastProjectName = lastProjectName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudent1Id() {
        return student1Id;
    }

    public void setStudent1Id(Long student1Id) {
        this.student1Id = student1Id;
    }

    public Long getStudent2Id() {
        return student2Id;
    }

    public void setStudent2Id(Long student2Id) {
        this.student2Id = student2Id;
    }

    public String getStudent1Name() {
        return student1Name;
    }

    public void setStudent1Name(String student1Name) {
        this.student1Name = student1Name;
    }

    public String getStudent2Name() {
        return student2Name;
    }

    public void setStudent2Name(String student2Name) {
        this.student2Name = student2Name;
    }

    public Integer getProjectsTogether() {
        return projectsTogether;
    }

    public void setProjectsTogether(Integer projectsTogether) {
        this.projectsTogether = projectsTogether;
    }

    public Integer getCompletedProjects() {
        return completedProjects;
    }

    public void setCompletedProjects(Integer completedProjects) {
        this.completedProjects = completedProjects;
    }

    public Double getCollaborationScore() {
        return collaborationScore;
    }

    public void setCollaborationScore(Double collaborationScore) {
        this.collaborationScore = collaborationScore;
    }

    public String getLastProjectName() {
        return lastProjectName;
    }

    public void setLastProjectName(String lastProjectName) {
        this.lastProjectName = lastProjectName;
    }
}
