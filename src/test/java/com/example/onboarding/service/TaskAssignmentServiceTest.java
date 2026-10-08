package com.example.onboarding.service;

import com.example.onboarding.entity.Department;
import com.example.onboarding.entity.Employee;
import com.example.onboarding.entity.Onboarding;
import com.example.onboarding.entity.OnboardingTask;
import com.example.onboarding.service.impl.TaskAssignmentServiceImpl;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskAssignmentServiceTest {

    private final TaskAssignmentService taskAssignmentService = new TaskAssignmentServiceImpl();

    @Test
    void testGenerateDefaultTasks_EngineeringDepartment() {
        Department dept = new Department("Engineering", "ENG", "Dev");
        Employee emp = new Employee();
        emp.setDepartment(dept);

        Onboarding onboarding = new Onboarding(emp, LocalDate.now(), LocalDate.now().plusDays(30));

        List<OnboardingTask> tasks = taskAssignmentService.generateDefaultTasks(emp, onboarding);

        // 4 base tasks + 1 engineering task
        assertEquals(5, tasks.size());
    }

    @Test
    void testGenerateDefaultTasks_OtherDepartment() {
        Department dept = new Department("Marketing", "MKT", "Marketing");
        Employee emp = new Employee();
        emp.setDepartment(dept);

        Onboarding onboarding = new Onboarding(emp, LocalDate.now(), LocalDate.now().plusDays(30));

        List<OnboardingTask> tasks = taskAssignmentService.generateDefaultTasks(emp, onboarding);

        // 4 base tasks only
        assertEquals(4, tasks.size());
    }
}
