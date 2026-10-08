package com.example.onboarding.service;

import com.example.onboarding.entity.Onboarding;

public interface WorkflowOrchestratorService {
    void recalculateProgress(Long onboardingId);
    void evaluateEscalations();
}
