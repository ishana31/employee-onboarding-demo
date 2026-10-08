package com.example.onboarding.service;

import com.example.onboarding.entity.Employee;
import com.example.onboarding.entity.Onboarding;
import com.example.onboarding.entity.OnboardingTask;
import com.example.onboarding.entity.enums.OnboardingStatus;
import com.example.onboarding.entity.enums.TaskCategory;
import com.example.onboarding.entity.enums.TaskPriority;
import com.example.onboarding.entity.enums.TaskStatus;
import com.example.onboarding.repository.OnboardingRepository;
import com.example.onboarding.service.impl.WorkflowOrchestratorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class WorkflowOrchestratorServiceTest {

    @Mock private OnboardingRepository onboardingRepository;
    @Mock private NotificationService notificationService;

    @InjectMocks private WorkflowOrchestratorServiceImpl workflowOrchestratorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRecalculateProgress_PartialCompletion() {
        Employee emp = new Employee();
        emp.setEmail("test@example.com");
        emp.setFirstName("John");

        Onboarding onboarding = new Onboarding(emp, LocalDate.now(), LocalDate.now().plusDays(30));
        onboarding.setId(1L);

        OnboardingTask t1 = new OnboardingTask("Task 1", "Desc", TaskCategory.IT_SETUP, TaskPriority.HIGH, LocalDate.now());
        t1.setStatus(TaskStatus.COMPLETED); // weight 2

        OnboardingTask t2 = new OnboardingTask("Task 2", "Desc", TaskCategory.HR_PAPERWORK, TaskPriority.HIGH, LocalDate.now());
        t2.setStatus(TaskStatus.PENDING); // weight 2

        onboarding.addTask(t1);
        onboarding.addTask(t2);

        when(onboardingRepository.findById(1L)).thenReturn(Optional.of(onboarding));

        workflowOrchestratorService.recalculateProgress(1L);

        assertEquals(50, onboarding.getCompletionPercentage());
        verify(onboardingRepository, times(1)).save(onboarding);
    }

    @Test
    void testRecalculateProgress_AllCompleted() {
        Employee emp = new Employee();
        emp.setEmail("test@example.com");
        emp.setFirstName("John");

        Onboarding onboarding = new Onboarding(emp, LocalDate.now(), LocalDate.now().plusDays(30));
        onboarding.setId(2L);

        OnboardingTask t1 = new OnboardingTask("Task 1", "Desc", TaskCategory.IT_SETUP, TaskPriority.HIGH, LocalDate.now());
        t1.setStatus(TaskStatus.COMPLETED);

        onboarding.addTask(t1);

        when(onboardingRepository.findById(2L)).thenReturn(Optional.of(onboarding));

        workflowOrchestratorService.recalculateProgress(2L);

        assertEquals(100, onboarding.getCompletionPercentage());
        assertEquals(OnboardingStatus.COMPLETED, onboarding.getStatus());
        verify(notificationService, times(1)).sendNotification(any(), any(), any(), any());
    }
}
