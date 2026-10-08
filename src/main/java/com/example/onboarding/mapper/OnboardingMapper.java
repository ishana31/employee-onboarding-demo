package com.example.onboarding.mapper;

import com.example.onboarding.dto.OnboardingDTO;
import com.example.onboarding.entity.Onboarding;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OnboardingMapper {
    private final OnboardingTaskMapper taskMapper;

    public OnboardingMapper(OnboardingTaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    public OnboardingDTO toDTO(Onboarding onboarding) {
        if (onboarding == null) return null;
        OnboardingDTO dto = new OnboardingDTO();
        dto.setId(onboarding.getId());
        if (onboarding.getEmployee() != null) {
            dto.setEmployeeId(onboarding.getEmployee().getId());
            dto.setEmployeeName(onboarding.getEmployee().getFullName());
        }
        dto.setStartDate(onboarding.getStartDate());
        dto.setTargetCompletionDate(onboarding.getTargetCompletionDate());
        dto.setStatus(onboarding.getStatus());
        dto.setCompletionPercentage(onboarding.getCompletionPercentage());
        if (onboarding.getTasks() != null) {
            dto.setTasks(onboarding.getTasks().stream().map(taskMapper::toDTO).collect(Collectors.toList()));
        }
        return dto;
    }
}
