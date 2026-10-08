package com.example.onboarding.service.impl;

import com.example.onboarding.entity.Employee;
import com.example.onboarding.entity.Onboarding;
import com.example.onboarding.entity.OnboardingTask;
import com.example.onboarding.entity.enums.TaskCategory;
import com.example.onboarding.entity.enums.TaskPriority;
import com.example.onboarding.service.TaskAssignmentService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskAssignmentServiceImpl implements TaskAssignmentService {

    @Override
    public List<OnboardingTask> generateDefaultTasks(Employee employee, Onboarding onboarding) {
        List<OnboardingTask> tasks = new ArrayList<>();
        LocalDate start = onboarding.getStartDate();

        // 1. Mandatory IT Setup
        OnboardingTask itTask = new OnboardingTask(
                "Configure Workstation & Accounts",
                "Set up hardware laptop, VPN, email client, and development tools.",
                TaskCategory.IT_SETUP,
                TaskPriority.HIGH,
                start.plusDays(2)
        );
        itTask.setOnboarding(onboarding);
        tasks.add(itTask);

        // 2. HR Documentation
        OnboardingTask hrTask = new OnboardingTask(
                "Submit Employment Paperwork",
                "Sign policy agreements, emergency contacts, and tax verification forms.",
                TaskCategory.HR_PAPERWORK,
                TaskPriority.HIGH,
                start.plusDays(3)
        );
        hrTask.setOnboarding(onboarding);
        tasks.add(hrTask);

        // 3. Security Compliance
        OnboardingTask secTask = new OnboardingTask(
                "Information Security Training",
                "Complete annual security compliance modules and phishing awareness.",
                TaskCategory.SECURITY_COMPLIANCE,
                TaskPriority.CRITICAL,
                start.plusDays(7)
        );
        secTask.setOnboarding(onboarding);
        tasks.add(secTask);

        // 4. Team Introduction
        OnboardingTask teamTask = new OnboardingTask(
                "Meet Team & Manager 1:1",
                "Introductory sync with direct manager and peers.",
                TaskCategory.TEAM_INTRODUCTION,
                TaskPriority.MEDIUM,
                start.plusDays(5)
        );
        teamTask.setOnboarding(onboarding);
        tasks.add(teamTask);

        // 5. Department-specific tasks
        if (employee.getDepartment() != null && "ENG".equalsIgnoreCase(employee.getDepartment().getCode())) {
            OnboardingTask engTask = new OnboardingTask(
                    "Repository Access and First Commit",
                    "Clone the repository, verify build pipelines, and push the first sanity pull request.",
                    TaskCategory.TRAINING,
                    TaskPriority.MEDIUM,
                    start.plusDays(10)
            );
            engTask.setOnboarding(onboarding);
            tasks.add(engTask);
        }

        return tasks;
    }
}
