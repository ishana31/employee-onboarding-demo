package com.example.onboarding.mapper;

import com.example.onboarding.dto.OnboardingTaskDTO;
import com.example.onboarding.entity.OnboardingTask;
import org.springframework.stereotype.Component;

@Component
public class OnboardingTaskMapper {
    public OnboardingTaskDTO toDTO(OnboardingTask task) {
        if (task == null) return null;
        OnboardingTaskDTO dto = new OnboardingTaskDTO();
        dto.setId(task.getId());
        if (task.getOnboarding() != null) {
            dto.setOnboardingId(task.getOnboarding().getId());
        }
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setCategory(task.getCategory());
        dto.setPriority(task.getPriority());
        dto.setStatus(task.getStatus());
        dto.setDueDate(task.getDueDate());
        dto.setCompletedAt(task.getCompletedAt());
        return dto;
    }
}
