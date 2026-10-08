package com.example.onboarding.service;

import com.example.onboarding.dto.CreateEmployeeRequest;
import com.example.onboarding.dto.EmployeeDTO;
import java.util.List;

public interface EmployeeService {
    EmployeeDTO createEmployee(CreateEmployeeRequest request);
    EmployeeDTO getEmployeeById(Long id);
    List<EmployeeDTO> getAllEmployees();
    List<EmployeeDTO> getEmployeesByDepartment(Long departmentId);
    void deactivateEmployee(Long id);
}
