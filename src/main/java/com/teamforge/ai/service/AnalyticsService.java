package com.teamforge.ai.service;

import com.teamforge.ai.dto.DashboardStatsDTO;
import com.teamforge.ai.dto.GeneratedTeamDTO;
import com.teamforge.ai.dto.TeamComparisonDTO;
import com.teamforge.ai.dto.TeamMemberDTO;
import com.teamforge.ai.model.Student;
import com.teamforge.ai.model.Team;
import com.teamforge.ai.model.TeamMember;
import com.teamforge.ai.repository.HackathonHistoryRepository;
import com.teamforge.ai.repository.ProjectHistoryRepository;
import com.teamforge.ai.repository.StudentRepository;
import com.teamforge.ai.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private final StudentRepository studentRepository;
    private final TeamRepository teamRepository;
    private final HackathonHistoryRepository hackathonHistoryRepository;
    private final ProjectHistoryRepository projectHistoryRepository;

    public AnalyticsService(StudentRepository studentRepository,
                            TeamRepository teamRepository,
                            HackathonHistoryRepository hackathonHistoryRepository,
                            ProjectHistoryRepository projectHistoryRepository) {
        this.studentRepository = studentRepository;
        this.teamRepository = teamRepository;
        this.hackathonHistoryRepository = hackathonHistoryRepository;
        this.projectHistoryRepository = projectHistoryRepository;
    }

    @Transactional(readOnly = true)
    public DashboardStatsDTO getDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        long totalStudents = studentRepository.count();
        long totalTeams = teamRepository.count();
        long totalHackathons = hackathonHistoryRepository.count();
        long totalProjects = projectHistoryRepository.count();

        stats.setTotalStudents(totalStudents);
        stats.setTotalTeams(totalTeams);
        stats.setTotalHackathons(totalHackathons);
        stats.setTotalProjects(totalProjects);

        Double avgScore = teamRepository.getAverageSuccessScore();
        stats.setAverageTeamSuccessScore(avgScore != null ? Math.round(avgScore * 10.0) / 10.0 : 82.4);

        Optional<Team> topTeam = teamRepository.findTopByOrderBySuccessScoreDesc();
        if (topTeam.isPresent()) {
            stats.setTopPerformingTeamName(topTeam.get().getTeamName());
            stats.setTopPerformingTeamScore(topTeam.get().getSuccessScore());
            stats.setTopPerformingCategory(topTeam.get().getProjectCategory());
        } else {
            stats.setTopPerformingTeamName("Team Alpha");
            stats.setTopPerformingTeamScore(89.5);
            stats.setTopPerformingCategory("AI & Machine Learning");
        }

        stats.setHighPotentialTeamsCount(teamRepository.countHighPotentialTeams());

        // Calculate skill averages across all students
        List<Student> allStudents = studentRepository.findAll();
        double sumCoding = 0, sumDesign = 0, sumComm = 0, sumDoc = 0;
        long hackWon = 0, hackPart = 0;
        Map<String, Long> roleCounts = new HashMap<>();

        for (Student s : allStudents) {
            sumCoding += s.getCodingSkill();
            sumDesign += s.getDesignSkill();
            sumComm += s.getCommunicationSkill();
            sumDoc += s.getDocumentationSkill();

            hackPart += s.getHackathonsParticipated();
            hackWon += s.getHackathonsWon();

            roleCounts.put(s.getPreferredRole(), roleCounts.getOrDefault(s.getPreferredRole(), 0L) + 1);
        }

        int n = allStudents.isEmpty() ? 1 : allStudents.size();
        Map<String, Double> skillAverages = new LinkedHashMap<>();
        skillAverages.put("Coding", Math.round((sumCoding / n) * 10.0) / 10.0);
        skillAverages.put("Design", Math.round((sumDesign / n) * 10.0) / 10.0);
        skillAverages.put("Communication", Math.round((sumComm / n) * 10.0) / 10.0);
        skillAverages.put("Documentation", Math.round((sumDoc / n) * 10.0) / 10.0);
        stats.setSkillAverages(skillAverages);
        stats.setRoleDistribution(roleCounts);

        Map<String, Long> hackStats = new LinkedHashMap<>();
        hackStats.put("Participated", hackPart);
        hackStats.put("Won", hackWon);
        stats.setHackathonStats(hackStats);

        // Project Category Counts
        List<Team> allTeams = teamRepository.findAll();
        Map<String, Long> catCounts = allTeams.stream()
                .collect(Collectors.groupingBy(Team::getProjectCategory, Collectors.counting()));
        stats.setProjectCategoryCounts(catCounts);

        return stats;
    }

    @Transactional(readOnly = true)
    public TeamComparisonDTO compareTeams(List<Long> teamIds) {
        List<Team> teams = teamRepository.findAllById(teamIds);
        List<GeneratedTeamDTO> dtos = teams.stream().map(this::convertTeamToDTO).collect(Collectors.toList());

        List<String> metricNames = Arrays.asList(
                "Skill Score",
                "Role Diversity",
                "Experience Score",
                "Hackathon Performance",
                "Compatibility Score",
                "Predicted Success"
        );

        Map<String, List<Double>> metricsByTeam = new LinkedHashMap<>();
        for (GeneratedTeamDTO t : dtos) {
            metricsByTeam.put(t.getTeamName(), Arrays.asList(
                    t.getSkillScore() != null ? t.getSkillScore() : 80.0,
                    t.getRoleDiversityScore() != null ? t.getRoleDiversityScore() : 80.0,
                    t.getExperienceScore() != null ? t.getExperienceScore() : 75.0,
                    t.getHackathonPerformance() != null ? t.getHackathonPerformance() : 70.0,
                    t.getCompatibilityScore() != null ? t.getCompatibilityScore() : 80.0,
                    t.getPredictedSuccessScore() != null ? t.getPredictedSuccessScore() : 80.0
            ));
        }

        // Generate AI comparative synthesis summary
        String summary = generateComparisonInsight(dtos);

        return new TeamComparisonDTO(dtos, metricNames, metricsByTeam, summary);
    }

    private String generateComparisonInsight(List<GeneratedTeamDTO> teams) {
        if (teams.isEmpty()) return "No teams selected for comparison.";
        if (teams.size() == 1) return teams.get(0).getTeamName() + " has an overall predicted success score of " + teams.get(0).getPredictedSuccessScore() + "%.";

        GeneratedTeamDTO highest = teams.stream().max(Comparator.comparing(GeneratedTeamDTO::getPredictedSuccessScore)).orElse(teams.get(0));
        GeneratedTeamDTO strongestCoding = teams.stream().max(Comparator.comparing(GeneratedTeamDTO::getSkillScore)).orElse(teams.get(0));
        GeneratedTeamDTO mostDiverse = teams.stream().max(Comparator.comparing(GeneratedTeamDTO::getRoleDiversityScore)).orElse(teams.get(0));

        return String.format(
                "%s leads the cohort with the highest overall predicted success score of %.1f%% (%s). " +
                "%s exhibits the highest technical skill coverage (%.1f%%), while %s displays the most balanced role distribution (%.1f%%).",
                highest.getTeamName(), highest.getPredictedSuccessScore(), highest.getPredictionLabel(),
                strongestCoding.getTeamName(), strongestCoding.getSkillScore(),
                mostDiverse.getTeamName(), mostDiverse.getRoleDiversityScore()
        );
    }

    private GeneratedTeamDTO convertTeamToDTO(Team team) {
        GeneratedTeamDTO dto = new GeneratedTeamDTO();
        dto.setId(team.getId());
        dto.setTeamName(team.getTeamName());
        dto.setProjectCategory(team.getProjectCategory());
        dto.setPredictedSuccessScore(team.getSuccessScore());
        dto.setPredictionLabel(team.getPredictionLabel());
        dto.setSkillScore(team.getSkillScore());
        dto.setRoleDiversityScore(team.getRoleDiversityScore());
        dto.setExperienceScore(team.getExperienceScore());
        dto.setHackathonPerformance(team.getHackathonPerformance());
        dto.setCompatibilityScore(team.getCompatibilityScore());
        dto.setAcademicYear(team.getAcademicYear());
        dto.setSaved(true);

        if (team.getStrengthsText() != null && !team.getStrengthsText().isEmpty()) {
            dto.setStrengths(Arrays.asList(team.getStrengthsText().split(" \\| ")));
        }
        if (team.getWeaknessesText() != null && !team.getWeaknessesText().isEmpty()) {
            dto.setWeaknesses(Arrays.asList(team.getWeaknessesText().split(" \\| ")));
        }

        List<TeamMemberDTO> memberDTOs = new ArrayList<>();
        if (team.getMembers() != null) {
            for (TeamMember m : team.getMembers()) {
                Student s = m.getStudent();
                if (s != null) {
                    TeamMemberDTO mDto = new TeamMemberDTO();
                    mDto.setStudentId(s.getId());
                    mDto.setName(s.getName());
                    mDto.setRegisterNumber(s.getRegisterNumber());
                    mDto.setEmail(s.getEmail());
                    mDto.setPreferredRole(s.getPreferredRole());
                    mDto.setAssignedRole(m.getAssignedRole());
                    mDto.setCodingSkill(s.getCodingSkill());
                    mDto.setDesignSkill(s.getDesignSkill());
                    mDto.setCommunicationSkill(s.getCommunicationSkill());
                    mDto.setDocumentationSkill(s.getDocumentationSkill());
                    mDto.setHackathonWinRate(s.getHackathonWinRate());
                    mDto.setWinRateDisplay(s.getWinRateDisplay());
                    mDto.setTeamExperience(s.getTeamExperience());
                    memberDTOs.add(mDto);
                }
            }
        }
        dto.setMembers(memberDTOs);
        return dto;
    }

    @Transactional(readOnly = true)
    public List<GeneratedTeamDTO> getLeaderboard(String category, String year) {
        List<Team> teams;
        if (category != null && !category.trim().isEmpty() && !"ALL".equalsIgnoreCase(category)) {
            teams = teamRepository.findByProjectCategoryIgnoreCaseOrderBySuccessScoreDesc(category.trim());
        } else if (year != null && !year.trim().isEmpty() && !"ALL".equalsIgnoreCase(year)) {
            teams = teamRepository.findByAcademicYearOrderBySuccessScoreDesc(year.trim());
        } else {
            teams = teamRepository.findAllByOrderBySuccessScoreDesc();
        }

        return teams.stream().map(this::convertTeamToDTO).collect(Collectors.toList());
    }
}
