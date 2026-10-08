package com.example.onboarding.mapper;

import com.example.onboarding.dto.NotificationDTO;
import com.example.onboarding.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    public NotificationDTO toDTO(Notification notification) {
        if (notification == null) return null;
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setRecipientEmail(notification.getRecipientEmail());
        dto.setSubject(notification.getSubject());
        dto.setContent(notification.getContent());
        dto.setType(notification.getType());
        dto.setDelivered(notification.isDelivered());
        dto.setCreatedAt(notification.getCreatedAt());
        return dto;
    }
}
