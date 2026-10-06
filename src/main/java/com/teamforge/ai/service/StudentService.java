package com.teamforge.ai.service;

import com.teamforge.ai.dto.HackathonHistoryDTO;
import com.teamforge.ai.dto.ProjectHistoryDTO;
import com.teamforge.ai.dto.StudentDTO;
import com.teamforge.ai.dto.StudentFormDTO;
import com.teamforge.ai.exception.ResourceNotFoundException;
import com.teamforge.ai.exception.ValidationException;
import com.teamforge.ai.model.HackathonHistory;
import com.teamforge.ai.model.ProjectHistory;
import com.teamforge.ai.model.Student;
import com.teamforge.ai.repository.HackathonHistoryRepository;
import com.teamforge.ai.repository.ProjectHistoryRepository;
import com.teamforge.ai.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final HackathonHistoryRepository hackathonHistoryRepository;
    private final ProjectHistoryRepository projectHistoryRepository;

    public StudentService(StudentRepository studentRepository, 
                          HackathonHistoryRepository hackathonHistoryRepository, 
                          ProjectHistoryRepository projectHistoryRepository) {
        this.studentRepository = studentRepository;
        this.hackathonHistoryRepository = hackathonHistoryRepository;
        this.projectHistoryRepository = projectHistoryRepository;
    }

    @Transactional(readOnly = true)
    public List<StudentDTO> getAllStudents(String search, String role, String status) {
        List<Student> students;

        if (search != null && !search.trim().isEmpty()) {
            students = studentRepository.searchStudents(search.trim());
        } else if (role != null && !role.trim().isEmpty() && !"ALL".equalsIgnoreCase(role)) {
            students = studentRepository.findByPreferredRole(role.trim());
        } else if (status != null && !status.trim().isEmpty()) {
            students = studentRepository.findByStatus(status.trim());
        } else {
            students = studentRepository.findAll();
        }

        return students.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        StudentDTO dto = mapToDTO(student);

        List<HackathonHistory> hackathons = hackathonHistoryRepository.findByStudentIdOrderByYearDesc(id);
        dto.setHackathonHistory(hackathons.stream()
                .map(h -> new HackathonHistoryDTO(h.getId(), h.getStudentId(), h.getHackathonName(), h.getYear(), h.getResult(), h.getTeamName()))
                .collect(Collectors.toList()));

        List<ProjectHistory> projects = projectHistoryRepository.findByStudentId(id);
        dto.setProjectHistory(projects.stream()
                .map(p -> new ProjectHistoryDTO(p.getId(), p.getStudentId(), p.getProjectName(), p.getTechnology(), p.getResult(), p.getTeamMembers()))
                .collect(Collectors.toList()));

        return dto;
    }

    @Transactional
    public StudentDTO createStudent(StudentFormDTO form) {
        form.validateBusinessRules();

        if (studentRepository.existsByRegisterNumber(form.getRegisterNumber().trim())) {
            throw new ValidationException("A student with Register Number '" + form.getRegisterNumber() + "' already exists.");
        }
        if (studentRepository.existsByEmail(form.getEmail().trim())) {
            throw new ValidationException("A student with Email '" + form.getEmail() + "' already exists.");
        }

        Student student = new Student(
                form.getName().trim(),
                form.getRegisterNumber().trim().toUpperCase(),
                form.getEmail().trim().toLowerCase(),
                form.getCodingSkill(),
                form.getDesignSkill(),
                form.getCommunicationSkill(),
                form.getDocumentationSkill(),
                form.getPreferredRole().trim(),
                form.getProjectInterests() != null ? form.getProjectInterests().trim() : "General Software Engineering",
                form.getHackathonsParticipated(),
                form.getHackathonsWon(),
                form.getPreviousProjects(),
                form.getProjectsCompleted(),
                form.getTeamExperience()
        );

        Student saved = studentRepository.save(student);
        return mapToDTO(saved);
    }

    @Transactional
    public StudentDTO updateStudent(Long id, StudentFormDTO form) {
        form.validateBusinessRules();

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));

        if (!student.getRegisterNumber().equalsIgnoreCase(form.getRegisterNumber().trim()) &&
            studentRepository.existsByRegisterNumber(form.getRegisterNumber().trim())) {
            throw new ValidationException("Register Number already in use by another student.");
        }
        if (!student.getEmail().equalsIgnoreCase(form.getEmail().trim()) &&
            studentRepository.existsByEmail(form.getEmail().trim())) {
            throw new ValidationException("Email already in use by another student.");
        }

        student.setName(form.getName().trim());
        student.setRegisterNumber(form.getRegisterNumber().trim().toUpperCase());
        student.setEmail(form.getEmail().trim().toLowerCase());
        student.setCodingSkill(form.getCodingSkill());
        student.setDesignSkill(form.getDesignSkill());
        student.setCommunicationSkill(form.getCommunicationSkill());
        student.setDocumentationSkill(form.getDocumentationSkill());
        student.setPreferredRole(form.getPreferredRole().trim());
        student.setProjectInterests(form.getProjectInterests() != null ? form.getProjectInterests().trim() : "");
        student.setHackathonsParticipated(form.getHackathonsParticipated());
        student.setHackathonsWon(form.getHackathonsWon());
        student.setPreviousProjects(form.getPreviousProjects());
        student.setProjectsCompleted(form.getProjectsCompleted());
        student.setTeamExperience(form.getTeamExperience());

        Student updated = studentRepository.save(student);
        return mapToDTO(updated);
    }

    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with ID: " + id);
        }
        studentRepository.deleteById(id);
    }

    /**
     * Parses and bulk imports students from CSV text.
     */
    @Transactional
    public Map<String, Object> importStudentsFromCsv(String csvContent) {
        if (csvContent == null || csvContent.trim().isEmpty()) {
            throw new ValidationException("CSV content cannot be empty.");
        }

        int importedCount = 0;
        int skippedCount = 0;
        List<String> errors = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new StringReader(csvContent))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (isHeader) {
                    isHeader = false;
                    if (line.toLowerCase().contains("name") && line.toLowerCase().contains("email")) {
                        continue;
                    }
                }

                String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (tokens.length < 5) {
                    skippedCount++;
                    errors.add("Line format invalid: " + line);
                    continue;
                }

                try {
                    String name = cleanToken(tokens[0]);
                    String regno = cleanToken(tokens[1]).toUpperCase();
                    String email = cleanToken(tokens[2]).toLowerCase();
                    int coding = parseSkill(tokens[3]);
                    int design = parseSkill(tokens[4]);
                    int comm = tokens.length > 5 ? parseSkill(tokens[5]) : 3;
                    int doc = tokens.length > 6 ? parseSkill(tokens[6]) : 3;
                    String role = tokens.length > 7 && !tokens[7].trim().isEmpty() ? cleanToken(tokens[7]) : "Developer";
                    String interests = tokens.length > 8 ? cleanToken(tokens[8]) : "Software Engineering";
                    int hackPart = tokens.length > 9 ? parsePositiveInt(tokens[9]) : 0;
                    int hackWon = tokens.length > 10 ? parsePositiveInt(tokens[10]) : 0;
                    int prevProj = tokens.length > 11 ? parsePositiveInt(tokens[11]) : 0;
                    int compProj = tokens.length > 12 ? parsePositiveInt(tokens[12]) : 0;
                    int teamExp = tokens.length > 13 ? parseSkill(tokens[13]) : 3;

                    if (studentRepository.existsByRegisterNumber(regno) || studentRepository.existsByEmail(email)) {
                        skippedCount++;
                        errors.add("Student already exists with regNo: " + regno);
                        continue;
                    }

                    Student student = new Student(
                            name, regno, email, coding, design, comm, doc,
                            role, interests, hackPart, hackWon, prevProj, compProj, teamExp
                    );
                    studentRepository.save(student);
                    importedCount++;
                } catch (Exception ex) {
                    skippedCount++;
                    errors.add("Error parsing line: " + ex.getMessage());
                }
            }
        } catch (Exception e) {
            throw new ValidationException("Failed to read CSV content: " + e.getMessage());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("importedCount", importedCount);
        result.put("skippedCount", skippedCount);
        result.put("errors", errors);
        return result;
    }

    private String cleanToken(String token) {
        if (token == null) return "";
        return token.replaceAll("^\"|\"$", "").trim();
    }

    private int parseSkill(String token) {
        try {
            int val = Integer.parseInt(cleanToken(token));
            return Math.max(1, Math.min(5, val));
        } catch (Exception e) {
            return 3;
        }
    }

    private int parsePositiveInt(String token) {
        try {
            int val = Integer.parseInt(cleanToken(token));
            return Math.max(0, val);
        } catch (Exception e) {
            return 0;
        }
    }

    public StudentDTO mapToDTO(Student s) {
        StudentDTO dto = new StudentDTO();
        dto.setId(s.getId());
        dto.setName(s.getName());
        dto.setRegisterNumber(s.getRegisterNumber());
        dto.setEmail(s.getEmail());
        dto.setCodingSkill(s.getCodingSkill());
        dto.setDesignSkill(s.getDesignSkill());
        dto.setCommunicationSkill(s.getCommunicationSkill());
        dto.setDocumentationSkill(s.getDocumentationSkill());
        dto.setPreferredRole(s.getPreferredRole());
        dto.setProjectInterests(s.getProjectInterests());
        dto.setHackathonsParticipated(s.getHackathonsParticipated());
        dto.setHackathonsWon(s.getHackathonsWon());
        dto.setPreviousProjects(s.getPreviousProjects());
        dto.setProjectsCompleted(s.getProjectsCompleted());
        dto.setTeamExperience(s.getTeamExperience());
        dto.setStatus(s.getStatus());
        dto.setHackathonWinRate(s.getHackathonWinRate());
        dto.setWinRateDisplay(s.getWinRateDisplay());
        dto.setProjectCompletionRate(s.getProjectCompletionRate());
        dto.setAverageSkill(s.getAverageSkill());
        dto.setCreatedAt(s.getCreatedAt());
        return dto;
    }
}
