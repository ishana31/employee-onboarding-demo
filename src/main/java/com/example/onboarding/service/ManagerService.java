package com.example.onboarding.service;

import com.example.onboarding.dto.CreateManagerRequest;
import com.example.onboarding.dto.ManagerDTO;
import java.util.List;

public interface ManagerService {
    ManagerDTO createManager(CreateManagerRequest request);
    ManagerDTO getManagerById(Long id);
    List<ManagerDTO> getAllManagers();
    List<ManagerDTO> getManagersByDepartment(Long departmentId);
}
