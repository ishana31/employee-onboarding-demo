package com.example.onboarding.mapper;

import com.example.onboarding.dto.ManagerDTO;
import com.example.onboarding.entity.Manager;
import org.springframework.stereotype.Component;

@Component
public class ManagerMapper {
    public ManagerDTO toDTO(Manager manager) {
        if (manager == null) return null;
        ManagerDTO dto = new ManagerDTO();
        dto.setId(manager.getId());
        dto.setFirstName(manager.getFirstName());
        dto.setLastName(manager.getLastName());
        dto.setEmail(manager.getEmail());
        dto.setTitle(manager.getTitle());
        if (manager.getDepartment() != null) {
            dto.setDepartmentId(manager.getDepartment().getId());
            dto.setDepartmentName(manager.getDepartment().getName());
        }
        return dto;
    }
}
