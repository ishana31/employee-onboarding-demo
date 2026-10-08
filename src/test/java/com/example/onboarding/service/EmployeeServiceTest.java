package com.example.onboarding.service;

import com.example.onboarding.dto.CreateEmployeeRequest;
import com.example.onboarding.dto.EmployeeDTO;
import com.example.onboarding.entity.Department;
import com.example.onboarding.entity.Employee;
import com.example.onboarding.mapper.EmployeeMapper;
import com.example.onboarding.repository.DepartmentRepository;
import com.example.onboarding.repository.EmployeeRepository;
import com.example.onboarding.repository.ManagerRepository;
import com.example.onboarding.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock private EmployeeRepository employeeRepository;
    @Mock private DepartmentRepository departmentRepository;
    @Mock private ManagerRepository managerRepository;
    @Mock private EmployeeMapper employeeMapper;
    @Mock private NotificationService notificationService;

    @InjectMocks private EmployeeServiceImpl employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEmployee_Success() {
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setFirstName("Alice");
        request.setLastName("Smith");
        request.setEmail("alice@example.com");
        request.setJobTitle("Software Engineer");
        request.setHireDate(LocalDate.now());
        request.setDepartmentId(1L);

        Department dept = new Department("Engineering", "ENG", "Dev team");
        dept.setId(1L);

        Employee saved = new Employee();
        saved.setId(10L);
        saved.setFirstName("Alice");
        saved.setLastName("Smith");
        saved.setEmail("alice@example.com");

        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(10L);
        dto.setFirstName("Alice");

        when(employeeRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(dept));
        when(employeeRepository.save(any(Employee.class))).thenReturn(saved);
        when(employeeMapper.toDTO(saved)).thenReturn(dto);

        EmployeeDTO result = employeeService.createEmployee(request);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        verify(notificationService, times(1)).sendNotification(any(), any(), any(), any());
    }
}
