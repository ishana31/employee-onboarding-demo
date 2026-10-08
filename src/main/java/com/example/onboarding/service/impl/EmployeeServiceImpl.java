package com.example.onboarding.service.impl;

import com.example.onboarding.dto.CreateEmployeeRequest;
import com.example.onboarding.dto.EmployeeDTO;
import com.example.onboarding.entity.Department;
import com.example.onboarding.entity.Employee;
import com.example.onboarding.entity.Manager;
import com.example.onboarding.entity.enums.NotificationType;
import com.example.onboarding.exception.DuplicateResourceException;
import com.example.onboarding.exception.ResourceNotFoundException;
import com.example.onboarding.mapper.EmployeeMapper;
import com.example.onboarding.repository.DepartmentRepository;
import com.example.onboarding.repository.EmployeeRepository;
import com.example.onboarding.repository.ManagerRepository;
import com.example.onboarding.service.EmployeeService;
import com.example.onboarding.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final ManagerRepository managerRepository;
    private final EmployeeMapper employeeMapper;
    private final NotificationService notificationService;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               DepartmentRepository departmentRepository,
                               ManagerRepository managerRepository,
                               EmployeeMapper employeeMapper,
                               NotificationService notificationService) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.managerRepository = managerRepository;
        this.employeeMapper = employeeMapper;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional
    public EmployeeDTO createEmployee(CreateEmployeeRequest request) {
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Employee with email " + request.getEmail() + " already exists");
        }

        Department dept = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + request.getDepartmentId()));

        Manager mgr = null;
        if (request.getManagerId() != null) {
            mgr = managerRepository.findById(request.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id " + request.getManagerId()));
        }

        Employee emp = new Employee();
        emp.setFirstName(request.getFirstName());
        emp.setLastName(request.getLastName());
        emp.setEmail(request.getEmail());
        emp.setJobTitle(request.getJobTitle());
        emp.setHireDate(request.getHireDate());
        emp.setActive(true);
        emp.setDepartment(dept);
        emp.setManager(mgr);

        Employee saved = employeeRepository.save(emp);

        notificationService.sendNotification(
                saved.getEmail(),
                "Welcome to the Organization",
                "Hello " + saved.getFirstName() + ", welcome to " + dept.getName() + " team!",
                NotificationType.WELCOME
        );

        return employeeMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeById(Long id) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
        return employeeMapper.toDTO(emp);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesByDepartment(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId).stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deactivateEmployee(Long id) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
        emp.setActive(false);
        employeeRepository.save(emp);
    }
}
