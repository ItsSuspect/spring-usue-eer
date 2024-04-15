package com.web.usue_eer.controllers.DisciplineCategory;

import com.web.usue_eer.entities.*;
import com.web.usue_eer.entities.enums.AccessType;
import com.web.usue_eer.payload.request.AdvertisementRequest;
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
    private final AdvertisementService advertisementService;
    private final UserDisciplineService userDisciplineService;
    private final UserNotificationService userNotificationService;
    private final UserDetailsServiceImpl userDetailsService;
    private final DisciplineService disciplineService;

    @Autowired
    public AnnouncementController(AdvertisementService advertisementService, UserDisciplineService userDisciplineService, UserNotificationService userNotificationService, UserDetailsServiceImpl userDetailsService, DisciplineService disciplineService) {
        this.advertisementService = advertisementService;
        this.userDisciplineService = userDisciplineService;
        this.userNotificationService = userNotificationService;
        this.userDetailsService = userDetailsService;
        this.disciplineService = disciplineService;
    }

    @GetMapping("/{disciplineId}/advertisements")
    public String getDisciplineAdvertisements(Model model, @PathVariable Long disciplineId) {
        List<Advertisement> advertisements = advertisementService.findAdvertisementsByDisciplineId(disciplineId);
        List<Advertisement> sortedAdvertisements = advertisements.stream()
                .sorted(Comparator.comparing(Advertisement::getDate))
                .collect(Collectors.toList());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy'г.'", new Locale("ru"));
        for (Advertisement advertisement : sortedAdvertisements) {
            String formattedDate = advertisement.getDate().format(formatter);
            advertisement.setFormattedDate(formattedDate);
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);

        UserDiscipline userDiscipline = userDisciplineService.findByDisciplineIdAndUserId(disciplineId, user.getId());

        model.addAttribute("advertisements", sortedAdvertisements);
        model.addAttribute("authorities", getAuthorities(userDiscipline.getAccessType()));
        model.addAttribute("content", "discipline-tabs/discipline-announcements/discipline-announcements");
        return "index";
    }

    @GetMapping("/{disciplineId}/advertisements/{advertisementId}/edit-advertisement")
    public String getEditDisciplineAdvertisements(Model model, @PathVariable Long disciplineId, @PathVariable Long advertisementId) {
        Advertisement advertisement = advertisementService.findAdvertisementById(advertisementId);

        model.addAttribute("advertisement", advertisement);
        model.addAttribute("content", "discipline-tabs/discipline-announcements/edit-announcement");
        return "index";
    }

    @PostMapping("/{disciplineId}/advertisements/{advertisementId}/edit-advertisement")
    public ResponseEntity<Void> editDisciplineAdvertisements(@PathVariable Long disciplineId, @PathVariable Long advertisementId, @RequestBody AdvertisementRequest advertisementRequest) {
        String owner = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(owner);

        Advertisement advertisement = advertisementService.findAdvertisementById(advertisementId);
        advertisement.setName(advertisementRequest.getName());
        advertisement.setContent(advertisementRequest.getContent());
        advertisement.setUser(user);

        advertisementService.saveAdvertisement(advertisement);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{disciplineId}/advertisements/create-advertisement")
    public String getCreateAdvertisement(Model model, @PathVariable Long disciplineId) {
        model.addAttribute("content", "discipline-tabs/discipline-announcements/create-announcement");
        return "index";
    }

    @PostMapping("/{disciplineId}/advertisements/create-advertisement")
    public ResponseEntity<Void> createAdvertisement(@PathVariable Long disciplineId, @RequestBody AdvertisementRequest advertisementRequest) {
        String owner = SecurityContextHolder.getContext().getAuthentication().getName();
        User userOwner = userDetailsService.findUserByUsername(owner);

        Discipline discipline = disciplineService.findDisciplineById(disciplineId);
        LocalDate currentDate = LocalDate.now();

        Advertisement advertisement = new Advertisement();
        advertisement.setName(advertisementRequest.getName());
        advertisement.setContent(advertisementRequest.getContent());
        advertisement.setDiscipline(discipline);
        advertisement.setDate(currentDate);
        advertisement.setUser(userOwner);

        advertisementService.saveAdvertisement(advertisement);
        String nameNotification = "Выложено объявление";
        List<User> users = userDetailsService.findByDisciplinesId(disciplineId);

        for(User user : users) {
            UserNotification userNotification = new UserNotification(user, nameNotification, "advertisement");
            userNotification.setAdvertisement(advertisement);
            userNotificationService.saveNotification(userNotification);
        }
        return ResponseEntity.ok().build();
    }

    public boolean getAuthorities(AccessType accessType) {
        return accessType.name().equals("LEADER");
    }
}