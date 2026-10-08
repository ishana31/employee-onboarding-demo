package com.example.onboarding.mapper;

import com.example.onboarding.dto.DepartmentDTO;
import com.example.onboarding.dto.CreateDepartmentRequest;
import com.example.onboarding.entity.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {
    public DepartmentDTO toDTO(Department department) {
        if (department == null) return null;
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setCode(department.getCode());
        dto.setDescription(department.getDescription());
        return dto;
    }

    public Department toEntity(CreateDepartmentRequest request) {
        if (request == null) return null;
        Department dept = new Department();
        dept.setName(request.getName());
        dept.setCode(request.getCode());
        dept.setDescription(request.getDescription());
        return dept;
    }
}
