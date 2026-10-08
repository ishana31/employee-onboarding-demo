package com.example.onboarding.service;

import com.example.onboarding.dto.OnboardingDTO;
import java.util.List;

public interface OnboardingService {
    OnboardingDTO initiateOnboarding(Long employeeId);
    OnboardingDTO getOnboardingByEmployeeId(Long employeeId);
    OnboardingDTO getOnboardingById(Long id);
    List<OnboardingDTO> getAllOnboardings();
}
