package com.teamforge.ai.controller;

import com.teamforge.ai.dto.CollaborationDTO;
import com.teamforge.ai.dto.HackathonHistoryDTO;
import com.teamforge.ai.dto.ProjectHistoryDTO;
import com.teamforge.ai.service.HistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/hackathons/student/{studentId}")
    public ResponseEntity<List<HackathonHistoryDTO>> getStudentHackathons(@PathVariable Long studentId) {
        return ResponseEntity.ok(historyService.getHackathonsByStudent(studentId));
    }

    @GetMapping("/projects/student/{studentId}")
    public ResponseEntity<List<ProjectHistoryDTO>> getStudentProjects(@PathVariable Long studentId) {
        return ResponseEntity.ok(historyService.getProjectsByStudent(studentId));
    }

    @GetMapping("/collaborations")
    public ResponseEntity<List<CollaborationDTO>> getAllCollaborations() {
        return ResponseEntity.ok(historyService.getAllCollaborations());
    }

    @GetMapping("/collaborations/student/{studentId}")
    public ResponseEntity<List<CollaborationDTO>> getStudentCollaborations(@PathVariable Long studentId) {
        return ResponseEntity.ok(historyService.getCollaborationsForStudent(studentId));
    }
}
