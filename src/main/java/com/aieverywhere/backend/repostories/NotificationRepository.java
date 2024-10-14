package com.aieverywhere.backend.repostories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aieverywhere.backend.models.Notifications;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, Long> {

    Long countByUserIdAndIsReadFalse(Long userId);

    List<Notifications> findByUserIdAndIsReadFalse(Long userId, Pageable pageable);
}
