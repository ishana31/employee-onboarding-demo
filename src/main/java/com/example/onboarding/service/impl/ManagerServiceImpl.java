package com.example.onboarding.service.impl;

import com.example.onboarding.dto.CreateManagerRequest;
import com.example.onboarding.dto.ManagerDTO;
import com.example.onboarding.entity.Department;
import com.example.onboarding.entity.Manager;
import com.example.onboarding.exception.DuplicateResourceException;
import com.example.onboarding.exception.ResourceNotFoundException;
import com.example.onboarding.mapper.ManagerMapper;
import com.example.onboarding.repository.DepartmentRepository;
import com.example.onboarding.repository.ManagerRepository;
import com.example.onboarding.service.ManagerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;
    private final DepartmentRepository departmentRepository;
    private final ManagerMapper managerMapper;

    public ManagerServiceImpl(ManagerRepository managerRepository, DepartmentRepository departmentRepository, ManagerMapper managerMapper) {
        this.managerRepository = managerRepository;
        this.departmentRepository = departmentRepository;
        this.managerMapper = managerMapper;
    }

    @Override
    @Transactional
    public ManagerDTO createManager(CreateManagerRequest request) {
        if (managerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Manager with email " + request.getEmail() + " already exists");
        }
        Department dept = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + request.getDepartmentId()));

        Manager mgr = new Manager();
        mgr.setFirstName(request.getFirstName());
        mgr.setLastName(request.getLastName());
        mgr.setEmail(request.getEmail());
        mgr.setTitle(request.getTitle());
        mgr.setDepartment(dept);

        Manager saved = managerRepository.save(mgr);
        return managerMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ManagerDTO getManagerById(Long id) {
        Manager mgr = managerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id " + id));
        return managerMapper.toDTO(mgr);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManagerDTO> getAllManagers() {
        return managerRepository.findAll().stream()
                .map(managerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManagerDTO> getManagersByDepartment(Long departmentId) {
        return managerRepository.findByDepartmentId(departmentId).stream()
                .map(managerMapper::toDTO)
                .collect(Collectors.toList());
    }
}
