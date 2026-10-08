package com.example.onboarding.service.impl;

import com.example.onboarding.dto.OnboardingDTO;
import com.example.onboarding.entity.Employee;
import com.example.onboarding.entity.Onboarding;
import com.example.onboarding.entity.OnboardingTask;
import com.example.onboarding.entity.enums.NotificationType;
import com.example.onboarding.entity.enums.OnboardingStatus;
import com.example.onboarding.exception.DuplicateResourceException;
import com.example.onboarding.exception.ResourceNotFoundException;
import com.example.onboarding.mapper.OnboardingMapper;
import com.example.onboarding.repository.EmployeeRepository;
import com.example.onboarding.repository.OnboardingRepository;
import com.example.onboarding.service.NotificationService;
import com.example.onboarding.service.OnboardingService;
import com.example.onboarding.service.TaskAssignmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OnboardingServiceImpl implements OnboardingService {

    private final OnboardingRepository onboardingRepository;
    private final EmployeeRepository employeeRepository;
    private final TaskAssignmentService taskAssignmentService;
    private final NotificationService notificationService;
    private final OnboardingMapper onboardingMapper;

    public OnboardingServiceImpl(OnboardingRepository onboardingRepository,
                                 EmployeeRepository employeeRepository,
                                 TaskAssignmentService taskAssignmentService,
                                 NotificationService notificationService,
                                 OnboardingMapper onboardingMapper) {
        this.onboardingRepository = onboardingRepository;
        this.employeeRepository = employeeRepository;
        this.taskAssignmentService = taskAssignmentService;
        this.notificationService = notificationService;
        this.onboardingMapper = onboardingMapper;
    }

    @Override
    @Transactional
    public OnboardingDTO initiateOnboarding(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + employeeId));

        if (onboardingRepository.findByEmployeeId(employeeId).isPresent()) {
            throw new DuplicateResourceException("Onboarding already exists for employee " + employeeId);
        }

        LocalDate startDate = employee.getHireDate() != null ? employee.getHireDate() : LocalDate.now();
        LocalDate targetDate = startDate.plusDays(30);

        Onboarding onboarding = new Onboarding(employee, startDate, targetDate);
        onboarding.setStatus(OnboardingStatus.IN_PROGRESS);

        List<OnboardingTask> tasks = taskAssignmentService.generateDefaultTasks(employee, onboarding);
        for (OnboardingTask task : tasks) {
            onboarding.addTask(task);
        }

        Onboarding saved = onboardingRepository.save(onboarding);

        if (employee.getManager() != null) {
            notificationService.sendNotification(
                    employee.getManager().getEmail(),
                    "Onboarding Initiated for " + employee.getFullName(),
                    "Onboarding program has started for " + employee.getFullName() + ". Please review their initial tasks.",
                    NotificationType.TASK_ASSIGNED
            );
        }

        return onboardingMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OnboardingDTO getOnboardingByEmployeeId(Long employeeId) {
        Onboarding onboarding = onboardingRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("No onboarding found for employee " + employeeId));
        return onboardingMapper.toDTO(onboarding);
    }

    @Override
    @Transactional(readOnly = true)
    public OnboardingDTO getOnboardingById(Long id) {
        Onboarding onboarding = onboardingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Onboarding not found with id " + id));
        return onboardingMapper.toDTO(onboarding);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OnboardingDTO> getAllOnboardings() {
        return onboardingRepository.findAll().stream()
                .map(onboardingMapper::toDTO)
                .collect(Collectors.toList());
    }
}
