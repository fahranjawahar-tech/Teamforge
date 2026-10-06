package com.teamforge.ai.controller;

import com.teamforge.ai.dto.EvaluateRosterRequest;
import com.teamforge.ai.dto.GeneratedTeamDTO;
import com.teamforge.ai.dto.TeamFormationRequest;
import com.teamforge.ai.model.Team;
import com.teamforge.ai.service.TeamFormationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamFormationService teamFormationService;

    public TeamController(TeamFormationService teamFormationService) {
        this.teamFormationService = teamFormationService;
    }

    @PostMapping("/generate")
    public ResponseEntity<List<GeneratedTeamDTO>> generateTeams(@RequestBody TeamFormationRequest request) {
        List<GeneratedTeamDTO> teams = teamFormationService.generateTeams(request);
        return ResponseEntity.ok(teams);
    }

    @PostMapping("/evaluate-roster")
    public ResponseEntity<GeneratedTeamDTO> evaluateRoster(@RequestBody EvaluateRosterRequest request) {
        GeneratedTeamDTO evaluated = teamFormationService.evaluateTeamRoster(
                request.getTeamName(),
                request.getStudentIds(),
                request.getProjectCategory(),
                request.getAcademicYear()
        );
        return ResponseEntity.ok(evaluated);
    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveTeam(@RequestBody GeneratedTeamDTO dto) {
        Team saved = teamFormationService.saveTeam(dto);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("teamId", saved.getId());
        response.put("teamName", saved.getTeamName());
        response.put("message", "Team " + saved.getTeamName() + " saved successfully!");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams() {
        return ResponseEntity.ok(teamFormationService.getAllSavedTeams());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable Long id) {
        Team team = teamFormationService.getTeamById(id);
        if (team != null) {
            return ResponseEntity.ok(team);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
