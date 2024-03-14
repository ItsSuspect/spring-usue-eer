package com.web.usue_eer.security.services;

import com.web.usue_eer.entities.UserNotification;
import com.web.usue_eer.repository.UserNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserNotificationService {
    @Autowired
    private UserNotificationRepository notificationRepository;

    @Transactional
    public void deleteUserNotificationById (Long id) {
        notificationRepository.deleteUserNotificationById(id);
    }

    public void saveNotification(UserNotification userNotification) {
        notificationRepository.save(userNotification);
    }

    public List<UserNotification> findUserNotificationsByUserId (Long id) {
        return notificationRepository.findUserNotificationsByUserId(id);
    }
}
