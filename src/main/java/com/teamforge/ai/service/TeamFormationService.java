package com.teamforge.ai.service;

import com.teamforge.ai.dto.*;
import com.teamforge.ai.exception.ValidationException;
import com.teamforge.ai.model.*;
import com.teamforge.ai.repository.StudentRepository;
import com.teamforge.ai.repository.TeamMemberRepository;
import com.teamforge.ai.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeamFormationService {

    private final StudentRepository studentRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final HistoryService historyService;
    private final MlPredictionClientService mlPredictionClient;

    private static final String[] TEAM_NAME_PREFIXES = {
        "Team Alpha", "Team Quantum", "Team Phoenix", "Team Nexus", "Team Apex",
        "Team Horizon", "Team Cyber", "Team Titan", "Team Nova", "Team Velocity",
        "Team Vortex", "Team Synergy", "Team Catalyst", "Team Zenith", "Team Aurora"
    };

    public TeamFormationService(StudentRepository studentRepository,
                                TeamRepository teamRepository,
                                TeamMemberRepository teamMemberRepository,
                                HistoryService historyService,
                                MlPredictionClientService mlPredictionClient) {
        this.studentRepository = studentRepository;
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.historyService = historyService;
        this.mlPredictionClient = mlPredictionClient;
    }

    /**
     * Core AI team formation algorithm generating balanced student teams.
     */
    public List<GeneratedTeamDTO> generateTeams(TeamFormationRequest request) {
        List<Student> pool;

        if (request.getSelectedStudentIds() != null && !request.getSelectedStudentIds().isEmpty()) {
            pool = studentRepository.findAllById(request.getSelectedStudentIds());
        } else {
            pool = studentRepository.findAll();
        }

        if (pool.isEmpty()) {
            throw new ValidationException("No available students found in the database to form teams.");
        }

        int totalStudents = pool.size();
        int teamSize = request.getTeamSize() != null && request.getTeamSize() > 0 ? request.getTeamSize() : 4;
        
        int numTeams;
        if (request.getNumberOfTeams() != null && request.getNumberOfTeams() > 0) {
            numTeams = request.getNumberOfTeams();
            if (numTeams > totalStudents) {
                throw new ValidationException("Number of teams (" + numTeams + ") cannot exceed total students (" + totalStudents + ").");
            }
        } else {
            numTeams = Math.max(1, totalStudents / teamSize);
        }

        if (numTeams <= 0) {
            numTeams = 1;
        }

        String priority = request.getSkillPriority() != null ? request.getSkillPriority().toUpperCase() : "BALANCED";
        Map<Student, Double> studentScores = new HashMap<>();
        for (Student s : pool) {
            double score = calculateCompositeScore(s, priority, request.getProjectCategory());
            studentScores.put(s, score);
        }

        List<Student> sortedStudents = new ArrayList<>(pool);
        sortedStudents.sort((s1, s2) -> Double.compare(studentScores.get(s2), studentScores.get(s1)));

        List<List<Student>> teamBuckets = new ArrayList<>();
        for (int i = 0; i < numTeams; i++) {
            teamBuckets.add(new ArrayList<>());
        }

        boolean forward = true;
        int bucketIndex = 0;

        for (Student s : sortedStudents) {
            teamBuckets.get(bucketIndex).add(s);
            if (forward) {
                bucketIndex++;
                if (bucketIndex >= numTeams) {
                    bucketIndex = numTeams - 1;
                    forward = false;
                }
            } else {
                bucketIndex--;
                if (bucketIndex < 0) {
                    bucketIndex = 0;
                    forward = true;
                }
            }
        }

        optimizeTeamBuckets(teamBuckets, request.getProjectCategory());

        List<GeneratedTeamDTO> resultTeams = new ArrayList<>();
        for (int i = 0; i < teamBuckets.size(); i++) {
            List<Student> members = teamBuckets.get(i);
            if (members.isEmpty()) continue;

            String teamName = i < TEAM_NAME_PREFIXES.length ? TEAM_NAME_PREFIXES[i] : "Team #" + (i + 1);
            GeneratedTeamDTO teamDTO = buildAndEvaluateTeam(teamName, members, request.getProjectCategory(), request.getAcademicYear());
            resultTeams.add(teamDTO);
        }

        resultTeams.sort((t1, t2) -> Double.compare(t2.getPredictedSuccessScore(), t1.getPredictedSuccessScore()));
        return resultTeams;
    }

    /**
     * Evaluates a custom dynamic student roster on the fly for interactive student swapping.
     */
    public GeneratedTeamDTO evaluateTeamRoster(String teamName, List<Long> studentIds, String category, String academicYear) {
        List<Student> students = studentRepository.findAllById(studentIds);
        if (students.isEmpty()) {
            throw new ValidationException("No valid students found for roster evaluation.");
        }
        return buildAndEvaluateTeam(teamName != null ? teamName : "Custom Team", students, category, academicYear);
    }

    private double calculateCompositeScore(Student s, String priority, String category) {
        double codingWeight = 0.30;
        double designWeight = 0.25;
        double commWeight = 0.25;
        double docWeight = 0.20;

        if ("CODING_HEAVY".equals(priority)) {
            codingWeight = 0.50;
            designWeight = 0.15;
            commWeight = 0.20;
            docWeight = 0.15;
        } else if ("DESIGN_HEAVY".equals(priority)) {
            codingWeight = 0.20;
            designWeight = 0.45;
            commWeight = 0.20;
            docWeight = 0.15;
        } else if ("INNOVATION_RESEARCH".equals(priority)) {
            codingWeight = 0.35;
            designWeight = 0.15;
            commWeight = 0.25;
            docWeight = 0.25;
        }

        double skillSum = (s.getCodingSkill() * codingWeight) +
                          (s.getDesignSkill() * designWeight) +
                          (s.getCommunicationSkill() * commWeight) +
                          (s.getDocumentationSkill() * docWeight);

        double hackWinBonus = (s.getHackathonWinRate() / 100.0) * 0.8;
        double expBonus = (s.getTeamExperience() / 5.0) * 0.5;
        double interestBonus = computeInterestMatch(s.getProjectInterests(), category) * 0.4;

        return skillSum + hackWinBonus + expBonus + interestBonus;
    }

    private void optimizeTeamBuckets(List<List<Student>> buckets, String category) {
        if (buckets.size() < 2) return;

        for (int i = 0; i < buckets.size() - 1; i++) {
            List<Student> b1 = buckets.get(i);
            List<Student> b2 = buckets.get(i + 1);

            for (int p1 = 0; p1 < b1.size(); p1++) {
                for (int p2 = 0; p2 < b2.size(); p2++) {
                    Student s1 = b1.get(p1);
                    Student s2 = b2.get(p2);

                    long rolesB1Before = b1.stream().map(Student::getPreferredRole).distinct().count();
                    long rolesB2Before = b2.stream().map(Student::getPreferredRole).distinct().count();

                    b1.set(p1, s2);
                    b2.set(p2, s1);

                    long rolesB1After = b1.stream().map(Student::getPreferredRole).distinct().count();
                    long rolesB2After = b2.stream().map(Student::getPreferredRole).distinct().count();

                    if ((rolesB1After + rolesB2After) < (rolesB1Before + rolesB2Before)) {
                        b1.set(p1, s1);
                        b2.set(p2, s2);
                    }
                }
            }
        }
    }

    private GeneratedTeamDTO buildAndEvaluateTeam(String teamName, List<Student> students, String category, String academicYear) {
        GeneratedTeamDTO dto = new GeneratedTeamDTO();
        dto.setTeamName(teamName);
        dto.setProjectCategory(category);
        dto.setAcademicYear(academicYear != null ? academicYear : "2025-2026");

        int n = students.size();
        double sumCoding = 0, sumDesign = 0, sumComm = 0, sumDoc = 0, sumExp = 0, sumWinRate = 0, sumCompRate = 0;
        Set<String> rolesSet = new HashSet<>();
        List<TeamMemberDTO> memberDTOs = new ArrayList<>();

        for (Student s : students) {
            sumCoding += s.getCodingSkill();
            sumDesign += s.getDesignSkill();
            sumComm += s.getCommunicationSkill();
            sumDoc += s.getDocumentationSkill();
            sumExp += s.getTeamExperience();
            sumWinRate += (s.getHackathonWinRate() / 100.0);
            sumCompRate += (s.getProjectCompletionRate() / 100.0);
            rolesSet.add(s.getPreferredRole());

            TeamMemberDTO mDto = new TeamMemberDTO();
            mDto.setStudentId(s.getId());
            mDto.setName(s.getName());
            mDto.setRegisterNumber(s.getRegisterNumber());
            mDto.setEmail(s.getEmail());
            mDto.setPreferredRole(s.getPreferredRole());
            mDto.setAssignedRole(assignOptimalRole(s, rolesSet));
            mDto.setCodingSkill(s.getCodingSkill());
            mDto.setDesignSkill(s.getDesignSkill());
            mDto.setCommunicationSkill(s.getCommunicationSkill());
            mDto.setDocumentationSkill(s.getDocumentationSkill());
            mDto.setHackathonWinRate(s.getHackathonWinRate());
            mDto.setWinRateDisplay(s.getWinRateDisplay());
            mDto.setTeamExperience(s.getTeamExperience());
            memberDTOs.add(mDto);
        }

        dto.setMembers(memberDTOs);

        double avgCoding = Math.round((sumCoding / n) * 10.0) / 10.0;
        double avgDesign = Math.round((sumDesign / n) * 10.0) / 10.0;
        double avgComm = Math.round((sumComm / n) * 10.0) / 10.0;
        double avgDoc = Math.round((sumDoc / n) * 10.0) / 10.0;
        double avgExp = Math.round((sumExp / n) * 10.0) / 10.0;
        double avgWinRate = Math.round((sumWinRate / n) * 1000.0) / 1000.0;
        double avgCompRate = Math.round((sumCompRate / n) * 1000.0) / 1000.0;

        dto.setAverageCoding(avgCoding);
        dto.setAverageDesign(avgDesign);
        dto.setAverageCommunication(avgComm);
        dto.setAverageDocumentation(avgDoc);
        dto.setAverageTeamExperience(avgExp);

        double roleDiversity = Math.min(1.0, (double) rolesSet.size() / (double) Math.min(n, 4));
        double interestSim = computeTeamInterestSimilarity(students, category);
        double collabScore = computeTeamCollaborationScore(students);

        MlPredictionRequest mlReq = new MlPredictionRequest();
        mlReq.setAverageCoding(avgCoding);
        mlReq.setAverageDesign(avgDesign);
        mlReq.setAverageCommunication(avgComm);
        mlReq.setAverageDocumentation(avgDoc);
        mlReq.setRoleDiversity(roleDiversity);
        mlReq.setInterestSimilarity(interestSim);
        mlReq.setCollaborationScore(collabScore / 100.0);
        mlReq.setTeamExperience(avgExp / 5.0);
        mlReq.setHackathonWinRate(avgWinRate);
        mlReq.setProjectCompletionRate(avgCompRate);

        MlPredictionResponse mlRes = mlPredictionClient.predictTeamPerformance(mlReq);

        dto.setPredictedSuccessScore(mlRes.getSuccessScore());
        dto.setPredictionLabel(mlRes.getPrediction());
        dto.setStrengths(mlRes.getStrengths());
        dto.setWeaknesses(mlRes.getWeaknesses());
        dto.setDiagnosticRecommendations(mlRes.getDiagnosticRecommendations());
        dto.setFeatureContributions(mlRes.getFeatureContributions());

        if (mlRes.getComponentScores() != null) {
            dto.setSkillScore(mlRes.getComponentScores().getOrDefault("skillScore", 80.0));
            dto.setRoleDiversityScore(mlRes.getComponentScores().getOrDefault("roleDiversity", 85.0));
            dto.setExperienceScore(mlRes.getComponentScores().getOrDefault("experienceScore", 75.0));
            dto.setHackathonPerformance(mlRes.getComponentScores().getOrDefault("hackathonPerformance", 70.0));
            dto.setCompatibilityScore(mlRes.getComponentScores().getOrDefault("compatibilityScore", 85.0));
        } else {
            dto.setSkillScore(Math.round(((avgCoding*0.35 + avgDesign*0.25 + avgComm*0.25 + avgDoc*0.15)/5.0)*1000.0)/10.0);
            dto.setRoleDiversityScore(Math.round(roleDiversity * 1000.0)/10.0);
            dto.setExperienceScore(Math.round(((avgExp/5.0)*0.5 + avgCompRate*0.5)*1000.0)/10.0);
            dto.setHackathonPerformance(Math.round(Math.max(30.0, avgWinRate*100.0)*10.0)/10.0);
            dto.setCompatibilityScore(Math.round(((interestSim*0.5 + (collabScore/100.0)*0.5))*1000.0)/10.0);
        }

        return dto;
    }

    private String assignOptimalRole(Student s, Set<String> teamRoles) {
        if (s.getPreferredRole() != null && !s.getPreferredRole().isEmpty()) {
            return s.getPreferredRole();
        }
        if (s.getCodingSkill() >= s.getDesignSkill() && s.getCodingSkill() >= s.getCommunicationSkill()) {
            return "Developer";
        } else if (s.getDesignSkill() >= s.getCodingSkill() && s.getDesignSkill() >= s.getCommunicationSkill()) {
            return "UI/UX Designer";
        } else if (s.getCommunicationSkill() >= 4 && s.getTeamExperience() >= 4) {
            return "Team Lead";
        } else {
            return "QA & Documentation Specialist";
        }
    }

    private double computeInterestMatch(String studentInterests, String targetCategory) {
        if (studentInterests == null || targetCategory == null) return 0.5;
        String s = studentInterests.toLowerCase();
        String t = targetCategory.toLowerCase();

        if (s.contains(t) || t.contains(s)) return 1.0;
        
        String[] keywords = t.split("[ &/,]+");
        int matches = 0;
        for (String kw : keywords) {
            if (kw.length() > 2 && s.contains(kw)) {
                matches++;
            }
        }
        return matches > 0 ? 0.85 : 0.45;
    }

    private double computeTeamInterestSimilarity(List<Student> students, String category) {
        if (students.isEmpty()) return 0.6;
        double sum = 0;
        for (Student s : students) {
            sum += computeInterestMatch(s.getProjectInterests(), category);
        }
        return Math.round((sum / students.size()) * 100.0) / 100.0;
    }

    private double computeTeamCollaborationScore(List<Student> students) {
        if (students.size() < 2) return 80.0;
        double totalScore = 0;
        int pairs = 0;

        for (int i = 0; i < students.size(); i++) {
            for (int j = i + 1; j < students.size(); j++) {
                double pairScore = historyService.getCollaborationScoreBetween(students.get(i).getId(), students.get(j).getId());
                totalScore += pairScore;
                pairs++;
            }
        }
        return pairs > 0 ? Math.round((totalScore / pairs) * 10.0) / 10.0 : 80.0;
    }

    @Transactional
    public Team saveTeam(GeneratedTeamDTO dto) {
        String strengthsJoined = dto.getStrengths() != null ? String.join(" | ", dto.getStrengths()) : "";
        String weaknessesJoined = dto.getWeaknesses() != null ? String.join(" | ", dto.getWeaknesses()) : "";

        Team team = new Team(
                dto.getTeamName(),
                dto.getProjectCategory(),
                dto.getPredictedSuccessScore(),
                dto.getPredictionLabel(),
                dto.getSkillScore(),
                dto.getRoleDiversityScore(),
                dto.getExperienceScore(),
                dto.getHackathonPerformance(),
                dto.getCompatibilityScore(),
                strengthsJoined,
                weaknessesJoined,
                dto.getAcademicYear()
        );

        Team savedTeam = teamRepository.save(team);

        List<Student> memberStudents = new ArrayList<>();
        if (dto.getMembers() != null) {
            for (TeamMemberDTO mDto : dto.getMembers()) {
                Student student = studentRepository.findById(mDto.getStudentId()).orElse(null);
                if (student != null) {
                    TeamMember member = new TeamMember(savedTeam, student, mDto.getAssignedRole());
                    teamMemberRepository.save(member);
                    memberStudents.add(student);
                    student.setStatus("ASSIGNED");
                    studentRepository.save(student);
                }
            }
        }

        for (int i = 0; i < memberStudents.size(); i++) {
            for (int j = i + 1; j < memberStudents.size(); j++) {
                historyService.recordCollaboration(
                        memberStudents.get(i).getId(),
                        memberStudents.get(j).getId(),
                        dto.getTeamName() + " - " + dto.getProjectCategory(),
                        true,
                        dto.getPredictedSuccessScore()
                );
            }
        }

        return savedTeam;
    }

    @Transactional(readOnly = true)
    public List<Team> getAllSavedTeams() {
        return teamRepository.findAllByOrderBySuccessScoreDesc();
    }

    @Transactional(readOnly = true)
    public Team getTeamById(Long id) {
        return teamRepository.findById(id).orElse(null);
    }
}
