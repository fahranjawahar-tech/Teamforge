package com.teamforge.ai.controller;

import com.teamforge.ai.dto.DashboardStatsDTO;
import com.teamforge.ai.dto.GeneratedTeamDTO;
import com.teamforge.ai.dto.TeamComparisonDTO;
import com.teamforge.ai.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        return ResponseEntity.ok(analyticsService.getDashboardStats());
    }

    @PostMapping("/compare")
    public ResponseEntity<TeamComparisonDTO> compareTeams(@RequestBody List<Long> teamIds) {
        return ResponseEntity.ok(analyticsService.compareTeams(teamIds));
    }

    @GetMapping("/rankings")
    public ResponseEntity<List<GeneratedTeamDTO>> getRankings(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String year) {
        return ResponseEntity.ok(analyticsService.getLeaderboard(category, year));
    }
}
