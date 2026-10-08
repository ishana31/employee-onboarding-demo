package com.example.onboarding.controller;

import com.example.onboarding.dto.CreateManagerRequest;
import com.example.onboarding.dto.ManagerDTO;
import com.example.onboarding.service.ManagerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/managers")
public class ManagerController {

    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @PostMapping
    public ResponseEntity<ManagerDTO> createManager(@Valid @RequestBody CreateManagerRequest request) {
        return new ResponseEntity<>(managerService.createManager(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManagerDTO> getManagerById(@PathVariable Long id) {
        return ResponseEntity.ok(managerService.getManagerById(id));
    }

    @GetMapping
    public ResponseEntity<List<ManagerDTO>> getAllManagers() {
        return ResponseEntity.ok(managerService.getAllManagers());
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<ManagerDTO>> getManagersByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(managerService.getManagersByDepartment(departmentId));
    }
}
