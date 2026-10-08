package com.example.onboarding.service.impl;

import com.example.onboarding.entity.Onboarding;
import com.example.onboarding.entity.OnboardingTask;
import com.example.onboarding.entity.enums.NotificationType;
import com.example.onboarding.entity.enums.OnboardingStatus;
import com.example.onboarding.entity.enums.TaskStatus;
import com.example.onboarding.exception.ResourceNotFoundException;
import com.example.onboarding.repository.OnboardingRepository;
import com.example.onboarding.service.NotificationService;
import com.example.onboarding.service.WorkflowOrchestratorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class WorkflowOrchestratorServiceImpl implements WorkflowOrchestratorService {

    private final OnboardingRepository onboardingRepository;
    private final NotificationService notificationService;

    public WorkflowOrchestratorServiceImpl(OnboardingRepository onboardingRepository,
                                           NotificationService notificationService) {
        this.onboardingRepository = onboardingRepository;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional
    public void recalculateProgress(Long onboardingId) {
        Onboarding onboarding = onboardingRepository.findById(onboardingId)
                .orElseThrow(() -> new ResourceNotFoundException("Onboarding not found with id " + onboardingId));

        List<OnboardingTask> tasks = onboarding.getTasks();
        if (tasks.isEmpty()) {
            onboarding.setCompletionPercentage(0);
            return;
        }

        int totalWeight = 0;
        int completedWeight = 0;

        for (OnboardingTask task : tasks) {
            int weight = switch (task.getPriority()) {
                case CRITICAL -> 3;
                case HIGH -> 2;
                case MEDIUM, LOW -> 1;
            };

            totalWeight += weight;
            if (task.getStatus() == TaskStatus.COMPLETED) {
                completedWeight += weight;
            }
        }

        int percentage = (int) Math.round(((double) completedWeight / totalWeight) * 100);
        onboarding.setCompletionPercentage(percentage);

        if (percentage == 100 && onboarding.getStatus() != OnboardingStatus.COMPLETED) {
            onboarding.setStatus(OnboardingStatus.COMPLETED);
            notificationService.sendNotification(
                    onboarding.getEmployee().getEmail(),
                    "Onboarding Completed!",
                    "Congratulations " + onboarding.getEmployee().getFirstName() + ", all onboarding tasks are finished!",
                    NotificationType.ONBOARDING_COMPLETED
            );
        } else if (percentage > 0 && onboarding.getStatus() == OnboardingStatus.NOT_STARTED) {
            onboarding.setStatus(OnboardingStatus.IN_PROGRESS);
        }

        onboardingRepository.save(onboarding);
    }

    @Override
    @Transactional
    public void evaluateEscalations() {
        List<Onboarding> activeOnboardings = onboardingRepository.findByStatus(OnboardingStatus.IN_PROGRESS);
        LocalDate today = LocalDate.now();

        for (Onboarding onboarding : activeOnboardings) {
            for (OnboardingTask task : onboarding.getTasks()) {
                if (task.getStatus() != TaskStatus.COMPLETED && task.getDueDate() != null && task.getDueDate().isBefore(today)) {
                    // Send reminder escalation to employee & manager
                    notificationService.sendNotification(
                            onboarding.getEmployee().getEmail(),
                            "Overdue Task Escalation: " + task.getTitle(),
                            "Please complete the task: " + task.getTitle() + " which was due on " + task.getDueDate(),
                            NotificationType.TASK_REMINDER
                    );
                    if (onboarding.getEmployee().getManager() != null) {
                        notificationService.sendNotification(
                                onboarding.getEmployee().getManager().getEmail(),
                                "Task Overdue for Direct Report: " + onboarding.getEmployee().getFullName(),
                                "The task '" + task.getTitle() + "' is currently overdue.",
                                NotificationType.TASK_REMINDER
                        );
                    }
                }
            }
        }
    }
}
