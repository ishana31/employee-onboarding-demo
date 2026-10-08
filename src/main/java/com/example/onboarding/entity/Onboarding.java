package com.example.onboarding.entity;

import com.example.onboarding.common.BaseEntity;
import com.example.onboarding.entity.enums.OnboardingStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "onboardings")
public class Onboarding extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false, unique = true)
    private Employee employee;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "target_completion_date")
    private LocalDate targetCompletionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OnboardingStatus status = OnboardingStatus.NOT_STARTED;

    @Column(name = "completion_percentage", nullable = false)
    private int completionPercentage = 0;

    @OneToMany(mappedBy = "onboarding", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OnboardingTask> tasks = new ArrayList<>();

    public Onboarding() {}

    public Onboarding(Employee employee, LocalDate startDate, LocalDate targetCompletionDate) {
        this.employee = employee;
        this.startDate = startDate;
        this.targetCompletionDate = targetCompletionDate;
        this.status = OnboardingStatus.NOT_STARTED;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getTargetCompletionDate() {
        return targetCompletionDate;
    }

    public void setTargetCompletionDate(LocalDate targetCompletionDate) {
        this.targetCompletionDate = targetCompletionDate;
    }

    public OnboardingStatus getStatus() {
        return status;
    }

    public void setStatus(OnboardingStatus status) {
        this.status = status;
    }

    public int getCompletionPercentage() {
        return completionPercentage;
    }

    public void setCompletionPercentage(int completionPercentage) {
        this.completionPercentage = completionPercentage;
    }

    public List<OnboardingTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<OnboardingTask> tasks) {
        this.tasks = tasks;
    }

    public void addTask(OnboardingTask task) {
        tasks.add(task);
        task.setOnboarding(this);
    }
}
