package com.teamforge.ai.controller;

import com.teamforge.ai.dto.MlPredictionRequest;
import com.teamforge.ai.dto.MlPredictionResponse;
import com.teamforge.ai.service.MlPredictionClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/prediction")
public class PredictionController {

    private final MlPredictionClientService mlPredictionClientService;

    public PredictionController(MlPredictionClientService mlPredictionClientService) {
        this.mlPredictionClientService = mlPredictionClientService;
    }

    @PostMapping("/predict")
    public ResponseEntity<MlPredictionResponse> predict(@RequestBody MlPredictionRequest request) {
        return ResponseEntity.ok(mlPredictionClientService.predictTeamPerformance(request));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> checkHealth() {
        Map<String, String> res = new HashMap<>();
        res.put("status", "UP");
        res.put("service", "TeamForge AI Spring Boot Engine");
        return ResponseEntity.ok(res);
    }
}
