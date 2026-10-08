package com.example.onboarding.repository;

import com.example.onboarding.entity.Onboarding;
import com.example.onboarding.entity.enums.OnboardingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OnboardingRepository extends JpaRepository<Onboarding, Long> {
    Optional<Onboarding> findByEmployeeId(Long employeeId);
    List<Onboarding> findByStatus(OnboardingStatus status);
}
