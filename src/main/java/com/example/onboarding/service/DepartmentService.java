package com.example.onboarding.service;

import com.example.onboarding.dto.DepartmentDTO;
import com.example.onboarding.dto.CreateDepartmentRequest;
import java.util.List;

public interface DepartmentService {
    DepartmentDTO createDepartment(CreateDepartmentRequest request);
    DepartmentDTO getDepartmentById(Long id);
    DepartmentDTO getDepartmentByCode(String code);
    List<DepartmentDTO> getAllDepartments();
    void deleteDepartment(Long id);
}
