package com.aieverywhere.backend.services;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.aieverywhere.backend.models.Notifications;
import com.aieverywhere.backend.repostories.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Notifications createNotification(Long userId, Long senderId,String contextType, Notifications.Type type, LocalDateTime createdAt ) {
        Notifications notification = new Notifications(userId, senderId,contextType, type , createdAt);
        return notificationRepository.save(notification);
    }

    public Long getUnreadNotificationCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    public List<Notifications> getUnreadNotifications(Long userId, int page, int size) {
        return notificationRepository.findByUserIdAndIsReadFalse(userId, PageRequest.of(page, size));
    }

    public void markAsRead(Long notificationId) {
        Notifications notification = notificationRepository.findById(notificationId).orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
