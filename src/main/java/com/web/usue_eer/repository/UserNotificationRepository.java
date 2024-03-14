package com.web.usue_eer.repository;

import com.web.usue_eer.entities.UserNotification;
import com.web.usue_eer.security.services.UserNotificationService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {
    List<UserNotification> findUserNotificationsByUserId(Long id);
    void deleteUserNotificationById(Long id);
}
