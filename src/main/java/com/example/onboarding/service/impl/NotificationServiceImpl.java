package com.example.onboarding.service.impl;

import com.example.onboarding.dto.NotificationDTO;
import com.example.onboarding.entity.Notification;
import com.example.onboarding.entity.enums.NotificationType;
import com.example.onboarding.mapper.NotificationMapper;
import com.example.onboarding.repository.NotificationRepository;
import com.example.onboarding.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public NotificationServiceImpl(NotificationRepository notificationRepository, NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    @Override
    @Transactional
    public NotificationDTO sendNotification(String to, String subject, String content, NotificationType type) {
        Notification notification = new Notification(to, subject, content, type);
        Notification saved = notificationRepository.save(notification);
        return notificationMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDTO> getNotificationsForUser(String email) {
        return notificationRepository.findByRecipientEmailOrderByCreatedAtDesc(email).stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }
}
