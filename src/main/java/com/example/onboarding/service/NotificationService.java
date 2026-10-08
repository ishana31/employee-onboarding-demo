package com.example.onboarding.service;

import com.example.onboarding.dto.NotificationDTO;
import com.example.onboarding.entity.enums.NotificationType;
import java.util.List;

public interface NotificationService {
    NotificationDTO sendNotification(String to, String subject, String content, NotificationType type);
    List<NotificationDTO> getNotificationsForUser(String email);
}
