package com.web.usue_eer.controllers.DisciplineCategory;

import com.web.usue_eer.entities.*;
import com.web.usue_eer.entities.enums.AccessType;
import com.web.usue_eer.payload.request.AnnouncementRequest;
import com.web.usue_eer.payload.response.AnnouncementResponse;
import com.web.usue_eer.payload.response.FilesResponse;
import com.web.usue_eer.security.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/portal/discipline")
public class AnnouncementController {
    private final AnnouncementService announcementService;
    private final UserDisciplineService userDisciplineService;
    private final UserNotificationService userNotificationService;
    private final UserDetailsServiceImpl userDetailsService;
    private final DisciplineService disciplineService;

    @Autowired
    public AnnouncementController(AnnouncementService announcementService, UserDisciplineService userDisciplineService, UserNotificationService userNotificationService, UserDetailsServiceImpl userDetailsService, DisciplineService disciplineService) {
        this.announcementService = announcementService;
        this.userDisciplineService = userDisciplineService;
        this.userNotificationService = userNotificationService;
        this.userDetailsService = userDetailsService;
        this.disciplineService = disciplineService;
    }

    @GetMapping("/{disciplineId}/announcements")
    public String getDisciplineAnnouncements(Model model, @PathVariable Long disciplineId) {
        List<Announcement> announcements = announcementService.findAnnouncementsByDisciplineId(disciplineId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy'г.'", new Locale("ru"));
        List<AnnouncementResponse> announcementResponses = announcements.stream()
                .map(announcement -> new AnnouncementResponse(
                        announcement.getId(),
                        announcement.getName(),
                        announcement.getContent(),
                        announcement.getDate().format(formatter),
                        announcement.getUser().getSurname() + ' ' + announcement.getUser().getName() + ' ' + announcement.getUser().getMiddleName()
                ))
                .sorted(Comparator.comparing(AnnouncementResponse::getDate).reversed())
                .collect(Collectors.toList());
        model.addAttribute("announcements", announcementResponses);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);

        UserDiscipline userDiscipline = userDisciplineService.findByDisciplineIdAndUserId(disciplineId, user.getId());
        model.addAttribute("authorities", getAuthorities(userDiscipline.getAccessType()));
        model.addAttribute("content", "discipline-tabs/discipline-announcements/discipline-announcements");
        return "index";
    }

    @GetMapping("/{disciplineId}/announcements/create")
    public String getCreateAnnouncement(Model model, @PathVariable Long disciplineId) {
        model.addAttribute("content", "discipline-tabs/discipline-announcements/create-announcement");
        return "index";
    }

    @PostMapping("/{disciplineId}/announcements/create")
    public ResponseEntity<Void> createAnnouncement(@PathVariable Long disciplineId, @RequestBody AnnouncementRequest announcementRequest) {
        String owner = SecurityContextHolder.getContext().getAuthentication().getName();
        User userOwner = userDetailsService.findUserByUsername(owner);

        Discipline discipline = disciplineService.findDisciplineById(disciplineId);
        LocalDate currentDate = LocalDate.now();

        Announcement announcement = new Announcement(
                discipline,
                announcementRequest.getName(),
                announcementRequest.getContent(),
                currentDate,
                userOwner
        );

        announcementService.saveAdvertisement(announcement);

        String nameNotification = "Выложено объявление";
        List<User> users = userDetailsService.findByDisciplinesId(disciplineId);
        for(User user : users) {
            UserNotification userNotification = new UserNotification(user, nameNotification, "announcement");
            userNotification.setAnnouncement(announcement);
            userNotificationService.saveNotification(userNotification);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{disciplineId}/announcements/{announcementId}/edit-announcement")
    public String getEditDisciplineAnnouncement(Model model, @PathVariable Long disciplineId, @PathVariable Long announcementId) {
        Announcement announcement = announcementService.findAnnouncementById(announcementId);

        model.addAttribute("announcement", announcement);
        model.addAttribute("content", "discipline-tabs/discipline-announcements/edit-announcement");
        return "index";
    }

    @PostMapping("/{disciplineId}/announcements/{announcementId}/edit-announcement")
    public ResponseEntity<Void> editDisciplineAdvertisements(@PathVariable Long disciplineId, @PathVariable Long announcementId, @RequestBody AnnouncementRequest announcementRequest) {
        String owner = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(owner);

        Announcement announcement = announcementService.findAnnouncementById(announcementId);
        announcement.setName(announcementRequest.getName());
        announcement.setContent(announcementRequest.getContent());
        announcement.setUser(user);

        announcementService.saveAdvertisement(announcement);
        return ResponseEntity.ok().build();
    }

    public boolean getAuthorities(AccessType accessType) {
        return accessType.name().equals("LEADER");
    }
}