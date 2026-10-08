package com.example.onboarding.controller;

import com.example.onboarding.dto.OnboardingDTO;
import com.example.onboarding.service.OnboardingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/onboarding")
public class OnboardingController {

    private final OnboardingService onboardingService;

    public OnboardingController(OnboardingService onboardingService) {
        this.onboardingService = onboardingService;
    }

    @PostMapping("/initiate/{employeeId}")
    public ResponseEntity<OnboardingDTO> initiateOnboarding(@PathVariable Long employeeId) {
        return new ResponseEntity<>(onboardingService.initiateOnboarding(employeeId), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OnboardingDTO> getOnboardingById(@PathVariable Long id) {
        return ResponseEntity.ok(onboardingService.getOnboardingById(id));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<OnboardingDTO> getOnboardingByEmployeeId(@PathVariable Long employeeId) {
        return ResponseEntity.ok(onboardingService.getOnboardingByEmployeeId(employeeId));
    }

    @GetMapping
    public ResponseEntity<List<OnboardingDTO>> getAllOnboardings() {
        return ResponseEntity.ok(onboardingService.getAllOnboardings());
    }
}
