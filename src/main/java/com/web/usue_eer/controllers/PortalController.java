package com.web.usue_eer.controllers;

import com.web.usue_eer.payload.request.*;
import com.web.usue_eer.security.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PortalController {
    private final UserNotificationService userNotificationService;

    @Autowired
    public PortalController(UserNotificationService userNotificationService) {
        this.userNotificationService = userNotificationService;
    }

    @GetMapping("")
    public String redirect() {
        return "redirect:/portal";
    }

    @GetMapping("/portal")
    public String index(Model model) {
        model.addAttribute("content", "common/main-page");
        return "index";
    }

    @GetMapping("/portal/discipline/{disciplineId}/{category}")
    public String getDisciplineCategoryContent(@PathVariable Long disciplineId, @PathVariable String category, Model model) {
        model.addAttribute("category", category);
        model.addAttribute("disciplineId", disciplineId);
        return "index";
    }

    @PostMapping("/portal/clear-notification")
    public ResponseEntity<Void> clearNotification(@RequestBody NotificationRequest notificationRequest) {
        userNotificationService.deleteUserNotificationById(notificationRequest.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/portal/clear-notifications-all")
    public ResponseEntity<Void> clearNotificationAll(@RequestBody NotificationRequest notificationRequest) {
        for (Long notificationId : notificationRequest.getNotificationIds()) {
            userNotificationService.deleteUserNotificationById(notificationId);
        }
        return ResponseEntity.ok().build();
    }
}