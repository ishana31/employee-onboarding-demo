package com.example.onboarding.service;

import com.example.onboarding.dto.OnboardingTaskDTO;
import com.example.onboarding.entity.enums.TaskStatus;
import java.util.List;

public interface OnboardingTaskService {
    OnboardingTaskDTO updateTaskStatus(Long taskId, TaskStatus status);
    List<OnboardingTaskDTO> getTasksByOnboarding(Long onboardingId);
}
