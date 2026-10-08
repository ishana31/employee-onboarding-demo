package com.example.onboarding.dto;

import com.example.onboarding.entity.enums.OnboardingStatus;
import java.time.LocalDate;
import java.util.List;

public class OnboardingDTO {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private LocalDate startDate;
    private LocalDate targetCompletionDate;
    private OnboardingStatus status;
    private int completionPercentage;
    private List<OnboardingTaskDTO> tasks;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getTargetCompletionDate() { return targetCompletionDate; }
    public void setTargetCompletionDate(LocalDate targetCompletionDate) { this.targetCompletionDate = targetCompletionDate; }
    public OnboardingStatus getStatus() { return status; }
    public void setStatus(OnboardingStatus status) { this.status = status; }
    public int getCompletionPercentage() { return completionPercentage; }
    public void setCompletionPercentage(int completionPercentage) { this.completionPercentage = completionPercentage; }
    public List<OnboardingTaskDTO> getTasks() { return tasks; }
    public void setTasks(List<OnboardingTaskDTO> tasks) { this.tasks = tasks; }
}
