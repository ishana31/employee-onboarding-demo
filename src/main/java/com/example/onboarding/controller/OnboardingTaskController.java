package com.example.onboarding.controller;

import com.example.onboarding.dto.OnboardingTaskDTO;
import com.example.onboarding.entity.enums.TaskStatus;
import com.example.onboarding.service.OnboardingTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class OnboardingTaskController {

    private final OnboardingTaskService taskService;

    public OnboardingTaskController(OnboardingTaskService taskService) {
        this.taskService = taskService;
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OnboardingTaskDTO> updateTaskStatus(@PathVariable Long id, @RequestParam TaskStatus status) {
        return ResponseEntity.ok(taskService.updateTaskStatus(id, status));
    }

    @GetMapping("/onboarding/{onboardingId}")
    public ResponseEntity<List<OnboardingTaskDTO>> getTasksByOnboarding(@PathVariable Long onboardingId) {
        return ResponseEntity.ok(taskService.getTasksByOnboarding(onboardingId));
    }
}
