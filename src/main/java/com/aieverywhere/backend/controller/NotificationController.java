package com.aieverywhere.backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aieverywhere.backend.dto.NotificationRequest;
import com.aieverywhere.backend.models.Notifications;
import com.aieverywhere.backend.models.Users;
import com.aieverywhere.backend.services.NotificationService;
import com.aieverywhere.backend.services.UsersServices;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UsersServices usersServices;


    @PostMapping
    public ResponseEntity<Notifications> createNotification(@RequestBody NotificationRequest notificationRequest) {
        Notifications notification = notificationService.createNotification(
                notificationRequest.getUserId(),
                notificationRequest.getSenderId(),
                notificationRequest.getContextType(),
                notificationRequest.getType(),
                notificationRequest.getCreatedAt()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(notification);
    }

     @GetMapping("/unread-count/{userId}")
    public ResponseEntity<Long> getUnreadNotificationCount(@PathVariable Long userId) {
        Long unreadCount = notificationService.getUnreadNotificationCount(userId);
        
        return ResponseEntity.ok(unreadCount);
    }

    @GetMapping("/unread/{userId}")
    public ResponseEntity<?> getUnreadNotifications(@PathVariable Long userId, @RequestParam int page, @RequestParam int size) {
        List<Notifications> unreadNotifications = notificationService.getUnreadNotifications(userId, page, size);
        List<NotificationRequest> notificationRequests = new ArrayList<>();
        try {
            for (Notifications notifications : unreadNotifications) {
                NotificationRequest request = new NotificationRequest();
                request.setNotificationId(notifications.getNotificationId());
                request.setUserId(notifications.getUserId());
                request.setSenderId(notifications.getSenderId());

                Users sender = usersServices.getUsersByUsersId(notifications.getSenderId());
                request.setUsername(sender.getUsername());
                request.setImagepath(sender.getImagePath());
                
                request.setContextType(notifications.getContextType());
                request.setCreatedAt(notifications.getCreatedAt());
                notificationRequests.add(request);
            }
            return ResponseEntity.ok(notificationRequests);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("failed to get unread notifications");
        }
        
        
    }

    @PutMapping("/mark-as-read/{notificationId}")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.noContent().build();
    }
}
