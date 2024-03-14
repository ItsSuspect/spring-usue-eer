package com.web.usue_eer.payload.request;

import java.util.List;

public class NotificationRequest {
    private Long id;

    private List<Long> notificationIds;

    public List<Long> getNotificationIds() {
        return notificationIds;
    }

    public void setNotificationIds(List<Long> notificationIds) {
        this.notificationIds = notificationIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
