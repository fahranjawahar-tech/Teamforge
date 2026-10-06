package com.teamforge.ai.service;

import com.teamforge.ai.dto.CollaborationDTO;
import com.teamforge.ai.dto.HackathonHistoryDTO;
import com.teamforge.ai.dto.ProjectHistoryDTO;
import com.teamforge.ai.model.CollaborationHistory;
import com.teamforge.ai.model.HackathonHistory;
import com.teamforge.ai.model.ProjectHistory;
import com.teamforge.ai.model.Student;
import com.teamforge.ai.repository.CollaborationHistoryRepository;
import com.teamforge.ai.repository.HackathonHistoryRepository;
import com.teamforge.ai.repository.ProjectHistoryRepository;
import com.teamforge.ai.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HistoryService {

    private final HackathonHistoryRepository hackathonHistoryRepository;
    private final ProjectHistoryRepository projectHistoryRepository;
    private final CollaborationHistoryRepository collaborationHistoryRepository;
    private final StudentRepository studentRepository;

    public HistoryService(HackathonHistoryRepository hackathonHistoryRepository,
                          ProjectHistoryRepository projectHistoryRepository,
                          CollaborationHistoryRepository collaborationHistoryRepository,
                          StudentRepository studentRepository) {
        this.hackathonHistoryRepository = hackathonHistoryRepository;
        this.projectHistoryRepository = projectHistoryRepository;
        this.collaborationHistoryRepository = collaborationHistoryRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional(readOnly = true)
    public List<HackathonHistoryDTO> getHackathonsByStudent(Long studentId) {
        return hackathonHistoryRepository.findByStudentIdOrderByYearDesc(studentId).stream()
                .map(h -> new HackathonHistoryDTO(h.getId(), h.getStudentId(), h.getHackathonName(), h.getYear(), h.getResult(), h.getTeamName()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProjectHistoryDTO> getProjectsByStudent(Long studentId) {
        return projectHistoryRepository.findByStudentId(studentId).stream()
                .map(p -> new ProjectHistoryDTO(p.getId(), p.getStudentId(), p.getProjectName(), p.getTechnology(), p.getResult(), p.getTeamMembers()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CollaborationDTO> getAllCollaborations() {
        return collaborationHistoryRepository.findAll().stream()
                .map(this::mapCollabToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CollaborationDTO> getCollaborationsForStudent(Long studentId) {
        return collaborationHistoryRepository.findByStudent(studentId).stream()
                .map(this::mapCollabToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public double getCollaborationScoreBetween(Long s1Id, Long s2Id) {
        Optional<CollaborationHistory> history = collaborationHistoryRepository.findBetweenStudents(s1Id, s2Id);
        return history.map(CollaborationHistory::getCollaborationScore).orElse(75.0); // Baseline neutral-positive score
    }

    @Transactional
    public void recordCollaboration(Long s1Id, Long s2Id, String projectName, boolean completed, double score) {
        if (s1Id.equals(s2Id)) return;

        Optional<CollaborationHistory> existing = collaborationHistoryRepository.findBetweenStudents(s1Id, s2Id);
        if (existing.isPresent()) {
            CollaborationHistory c = existing.get();
            c.setProjectsTogether(c.getProjectsTogether() + 1);
            if (completed) {
                c.setCompletedProjects(c.getCompletedProjects() + 1);
            }
            // Moving average of collaboration score
            c.setCollaborationScore(Math.round(((c.getCollaborationScore() * 0.6) + (score * 0.4)) * 10.0) / 10.0);
            c.setLastProjectName(projectName);
            collaborationHistoryRepository.save(c);
        } else {
            Student s1 = studentRepository.findById(s1Id).orElse(null);
            Student s2 = studentRepository.findById(s2Id).orElse(null);

            String s1Name = s1 != null ? s1.getName() : "Student #" + s1Id;
            String s2Name = s2 != null ? s2.getName() : "Student #" + s2Id;

            CollaborationHistory newCollab = new CollaborationHistory(
                    s1Id, s2Id, s1Name, s2Name,
                    1,
                    completed ? 1 : 0,
                    score,
                    projectName
            );
            collaborationHistoryRepository.save(newCollab);
        }
    }

    private CollaborationDTO mapCollabToDTO(CollaborationHistory c) {
        return new CollaborationDTO(
                c.getId(),
                c.getStudent1Id(),
                c.getStudent2Id(),
                c.getStudent1Name(),
                c.getStudent2Name(),
                c.getProjectsTogether(),
                c.getCompletedProjects(),
                c.getCollaborationScore(),
                c.getLastProjectName()
        );
    }
}
