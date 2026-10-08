package com.example.onboarding.repository;

import com.example.onboarding.entity.OnboardingTask;
import com.example.onboarding.entity.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OnboardingTaskRepository extends JpaRepository<OnboardingTask, Long> {
    List<OnboardingTask> findByOnboardingId(Long onboardingId);
    List<OnboardingTask> findByOnboardingIdAndStatus(Long onboardingId, TaskStatus status);
}
