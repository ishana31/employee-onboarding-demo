package com.example.onboarding.service.impl;

import com.example.onboarding.dto.OnboardingTaskDTO;
import com.example.onboarding.entity.Onboarding;
import com.example.onboarding.entity.OnboardingTask;
import com.example.onboarding.entity.enums.TaskStatus;
import com.example.onboarding.exception.ResourceNotFoundException;
import com.example.onboarding.mapper.OnboardingTaskMapper;
import com.example.onboarding.repository.OnboardingRepository;
import com.example.onboarding.repository.OnboardingTaskRepository;
import com.example.onboarding.service.OnboardingTaskService;
import com.example.onboarding.service.WorkflowOrchestratorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OnboardingTaskServiceImpl implements OnboardingTaskService {

    private final OnboardingTaskRepository taskRepository;
    private final OnboardingRepository onboardingRepository;
    private final OnboardingTaskMapper taskMapper;
    private final WorkflowOrchestratorService workflowOrchestratorService;

    public OnboardingTaskServiceImpl(OnboardingTaskRepository taskRepository,
                                     OnboardingRepository onboardingRepository,
                                     OnboardingTaskMapper taskMapper,
                                     WorkflowOrchestratorService workflowOrchestratorService) {
        this.taskRepository = taskRepository;
        this.onboardingRepository = onboardingRepository;
        this.taskMapper = taskMapper;
        this.workflowOrchestratorService = workflowOrchestratorService;
    }

    @Override
    @Transactional
    public OnboardingTaskDTO updateTaskStatus(Long taskId, TaskStatus status) {
        OnboardingTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId));

        task.setStatus(status);
        if (status == TaskStatus.COMPLETED) {
            task.setCompletedAt(LocalDateTime.now());
        }

        OnboardingTask saved = taskRepository.save(task);

        // Recalculate workflow progress
        workflowOrchestratorService.recalculateProgress(task.getOnboarding().getId());

        return taskMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OnboardingTaskDTO> getTasksByOnboarding(Long onboardingId) {
        return taskRepository.findByOnboardingId(onboardingId).stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
    }
}
