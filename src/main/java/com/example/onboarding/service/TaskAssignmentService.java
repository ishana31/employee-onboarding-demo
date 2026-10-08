package com.example.onboarding.service;

import com.example.onboarding.entity.Employee;
import com.example.onboarding.entity.Onboarding;
import com.example.onboarding.entity.OnboardingTask;
import java.util.List;

public interface TaskAssignmentService {
    List<OnboardingTask> generateDefaultTasks(Employee employee, Onboarding onboarding);
}
