package com.web.usue_eer.controllers;

import com.web.usue_eer.entities.*;
import com.web.usue_eer.entities.enums.AccessType;
import com.web.usue_eer.payload.request.AdvertisementRequest;
import com.web.usue_eer.payload.request.DisciplineRequest;
import com.web.usue_eer.payload.request.InformationRequest;
import com.web.usue_eer.payload.request.NotificationRequest;
import com.web.usue_eer.payload.response.UserResponse;
import com.web.usue_eer.security.services.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/portal")
public class PortalController {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private DisciplineService disciplineService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserTaskService userTaskService;

    @Autowired
    private UserDisciplineService userDisciplineService;

    @Autowired
    private AdvertisementService advertisementService;

    @Autowired
    private UserNotificationService userNotificationService;

    @Autowired
    private InformationService informationService;

    @GetMapping("")
    public String index(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);
        List<UserNotification> userNotifications = userNotificationService.findUserNotificationsByUserId(user.getId());

        model.addAttribute("notifications", userNotifications);
        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("content", "fragments/main-information");
        return "index";
    }

    @PostMapping("/clear-notification")
    public ResponseEntity<Void> clearNotification(@RequestBody NotificationRequest notificationRequest) {
        userNotificationService.deleteUserNotificationById(notificationRequest.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/clear-notifications-all")
    public ResponseEntity<Void> clearNotificationAll(@RequestBody NotificationRequest notificationRequest) {
        for (Long notificationId : notificationRequest.getNotificationIds()) {
            userNotificationService.deleteUserNotificationById(notificationId);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/disciplines")
    public String disciplines(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);
        List<UserNotification> userNotifications = userNotificationService.findUserNotificationsByUserId(user.getId());

        model.addAttribute("notifications", userNotifications);
        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("content", "fragments/disciplines");
        return "index";
    }

    @GetMapping("/disciplines/create")
    public String createDisciplines(Model model) {
        List<Group> groups = groupService.findAllGroups();
        List<User> users = userDetailsService.findAllUsers();

        List<UserResponse> userResponses = users.stream()
                .map(user -> {
                    UserResponse dto = new UserResponse();
                    dto.setId(user.getId());
                    dto.setUsername(user.getUsername());
                    dto.setEmail(user.getEmail());
                    dto.setName(user.getName());
                    dto.setSurname(user.getSurname());
                    dto.setMiddleName(user.getMiddleName());
                    dto.setGroups(user.getGroups());
                    return dto;
                })
                .collect(Collectors.toList());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);
        List<UserNotification> userNotifications = userNotificationService.findUserNotificationsByUserId(user.getId());

        model.addAttribute("notifications", userNotifications);
        model.addAttribute("groups", groups);
        model.addAttribute("users", userResponses);
        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("content", "fragments/create-discipline");
        return "index";
    }

    @PostMapping("/disciplines/create-getUserGroup")
    public ResponseEntity<List<UserResponse>> getUsersGroup(@RequestBody Group group) {
        List<User> users = userDetailsService.findUsersByGroupName(group.getName());

        List<UserResponse> userResponses = users.stream()
                .map(user -> {
                    UserResponse dto = new UserResponse();
                    dto.setId(user.getId());
                    dto.setUsername(user.getUsername());
                    dto.setEmail(user.getEmail());
                    dto.setName(user.getName());
                    dto.setSurname(user.getSurname());
                    dto.setMiddleName(user.getMiddleName());
                    dto.setGroups(user.getGroups());
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(userResponses);
    }

    @PostMapping("/disciplines/create")
    public ResponseEntity<Discipline> createDiscipline(@Valid @RequestBody DisciplineRequest disciplineRequest) {
        String owner = SecurityContextHolder.getContext().getAuthentication().getName();

        Discipline discipline = disciplineService.saveDiscipline(new Discipline(disciplineRequest.getName(), owner));
        Map<String, String> usersWithAccess = disciplineRequest.getUsers();

        for (Map.Entry<String, String> entry : usersWithAccess.entrySet()) {
            String username = entry.getKey();
            AccessType access = AccessType.valueOf(entry.getValue());

            User user = userDetailsService.findUserByUsername(username);
            if (user != null) {
                userDisciplineService.saveAccess(new UserDiscipline(user, discipline, access));
            }
        }

        User ownerUser = userDetailsService.findUserByUsername(owner);
        if (ownerUser != null) {
            userDisciplineService.saveAccess(new UserDiscipline(ownerUser, discipline, AccessType.LEADER));
        }
        return ResponseEntity.ok(discipline);
    }

    @GetMapping("/discipline/{disciplineId}/{category}")
    public String getDisciplineCategoryContent(@PathVariable("disciplineId") Long disciplineId,
                                               @PathVariable("category") String category,
                                               Model model) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);
        List<UserNotification> userNotifications = userNotificationService.findUserNotificationsByUserId(user.getId());

        model.addAttribute("notifications", userNotifications);
        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("category", category);
        model.addAttribute("disciplineId", disciplineId);
        return "index";
    }

    @GetMapping("/discipline/{disciplineId}/member-list")
    public String getMemberList(Model model, @PathVariable String disciplineId) {
        List<User> users = userDetailsService.findByDisciplinesId(Long.parseLong(disciplineId));
        Map<String, List<User>> usersByGroup = users.stream()
                .collect(Collectors.groupingBy(user -> user.getGroups().iterator().next().getName()));

        users.forEach(user -> {
            AccessType accessType = userDisciplineService.findByDisciplineIdAndUserId(Long.parseLong(disciplineId), user.getId()).getAccessType();
            user.setAccessType(accessType == AccessType.PARTICIPANT ? "Участник" : "Руководитель");
        });

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);

        UserDiscipline userDiscipline = userDisciplineService.findByDisciplineIdAndUserId(Long.parseLong(disciplineId), user.getId());
        List<UserNotification> userNotifications = userNotificationService.findUserNotificationsByUserId(user.getId());

        model.addAttribute("notifications", userNotifications);
        model.addAttribute("usersByGroup", usersByGroup);
        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("users", users);
        model.addAttribute("authorities", getAuthorities(userDiscipline.getAccessType()));
        model.addAttribute("content", "fragments/member-list");
        return "index";
    }

    @GetMapping("/discipline/{disciplineId}/information")
    public String getDisciplineInformation(Model model, @PathVariable String disciplineId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);

        UserDiscipline userDiscipline = userDisciplineService.findByDisciplineIdAndUserId(Long.parseLong(disciplineId), user.getId());
        List<UserNotification> userNotifications = userNotificationService.findUserNotificationsByUserId(user.getId());

        Information information = informationService.findInformationByDisciplineId(Long.parseLong(disciplineId))
                .orElseGet(() -> {
                    Information defaultInformation = new Information();
                    defaultInformation.setInformationOfDiscipline("Преподаватель скоро предоставит информацию");
                    defaultInformation.setInformationOfTeacher("Преподаватель скоро предоставит информацию");
                    defaultInformation.setContacts("Преподаватель скоро предоставит информацию");
                    return defaultInformation;
                });

        model.addAttribute("information", information);
        model.addAttribute("notifications", userNotifications);
        model.addAttribute("authorities", getAuthorities(userDiscipline.getAccessType()));
        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("content", "fragments/basic-information");
        return "index";
    }

    @GetMapping("/discipline/{disciplineId}/advertisements")
    public String getDisciplineAdvertisements(Model model, @PathVariable String disciplineId) {
        List<Advertisement> advertisements = advertisementService.findAdvertisementsByDisciplineId(Long.parseLong(disciplineId));
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

        UserDiscipline userDiscipline = userDisciplineService.findByDisciplineIdAndUserId(Long.parseLong(disciplineId), user.getId());
        List<UserNotification> userNotifications = userNotificationService.findUserNotificationsByUserId(user.getId());

        model.addAttribute("notifications", userNotifications);
        model.addAttribute("advertisements", sortedAdvertisements);
        model.addAttribute("authorities", getAuthorities(userDiscipline.getAccessType()));
        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("content", "fragments/advertisements");
        return "index";
    }

    @GetMapping("/discipline/{disciplineId}/advertisements/{advertisementId}/edit-advertisement")
    public String getEditDisciplineAdvertisements(Model model, @PathVariable String disciplineId, @PathVariable String advertisementId) {
        Advertisement advertisement = advertisementService.findAdvertisementById(Long.parseLong(advertisementId));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);
        List<UserNotification> userNotifications = userNotificationService.findUserNotificationsByUserId(user.getId());

        model.addAttribute("notifications", userNotifications);
        model.addAttribute("advertisement", advertisement);
        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("content", "fragments/edit-advertisement");
        return "index";
    }

    @PostMapping("/discipline/{disciplineId}/advertisements/{advertisementId}/edit-advertisement")
    public ResponseEntity<Void> editDisciplineAdvertisements(Model model, @PathVariable String disciplineId, @PathVariable String advertisementId, @RequestBody AdvertisementRequest advertisementRequest) {
        String owner = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(owner);

        Advertisement advertisement = advertisementService.findAdvertisementById(Long.parseLong(advertisementId));
        advertisement.setName(advertisementRequest.getName());
        advertisement.setContent(advertisementRequest.getContent());
        advertisement.setUser(user);

        advertisementService.saveAdvertisement(advertisement);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/discipline/{disciplineId}/advertisements/create-advertisement")
    public String getCreateAdvertisement(Model model, @PathVariable String disciplineId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);
        List<UserNotification> userNotifications = userNotificationService.findUserNotificationsByUserId(user.getId());

        model.addAttribute("notifications", userNotifications);
        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("content", "fragments/create-advertisement");
        return "index";
    }

    @PostMapping("/discipline/{disciplineId}/advertisements/create-advertisement")
    public ResponseEntity<Void> createAdvertisement(Model model, @PathVariable String disciplineId, @RequestBody AdvertisementRequest advertisementRequest) {
        String owner = SecurityContextHolder.getContext().getAuthentication().getName();
        User userOwner = userDetailsService.findUserByUsername(owner);

        Discipline discipline = disciplineService.findDisciplineById(Long.parseLong(disciplineId));
        LocalDate currentDate = LocalDate.now();

        Advertisement advertisement = new Advertisement();
        advertisement.setName(advertisementRequest.getName());
        advertisement.setContent(advertisementRequest.getContent());
        advertisement.setDiscipline(discipline);
        advertisement.setDate(currentDate);
        advertisement.setUser(userOwner);

        advertisementService.saveAdvertisement(advertisement);
        String nameNotification = "Выложено объявление";
        List<User> users = userDetailsService.findByDisciplinesId(Long.parseLong(disciplineId));

        for(User user : users) {
            UserNotification userNotification = new UserNotification(user, nameNotification, "advertisement");
            userNotification.setAdvertisement(advertisement);
            userNotificationService.saveNotification(userNotification);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/discipline/{disciplineId}/information/edit-information")
    public String getEditInformation(Model model, @PathVariable String disciplineId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);
        List<UserNotification> userNotifications = userNotificationService.findUserNotificationsByUserId(user.getId());

        model.addAttribute("notifications", userNotifications);
        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("content", "fragments/enter-basic-information");
        return "index";
    }

    @PostMapping("/discipline/{disciplineId}/information/edit-information")
    public ResponseEntity<Void> editInformation(Model model, @PathVariable String disciplineId, @RequestBody InformationRequest informationRequest) {
        Discipline discipline = disciplineService.findDisciplineById(Long.parseLong(disciplineId));
        Information information = new Information(
                informationRequest.getInformationOfDiscipline(),
                informationRequest.getInformationOfTeacher(),
                informationRequest.getContacts(),
                discipline
        );
        informationService.saveInformation(information);
        return ResponseEntity.ok().build();
    }

    public boolean getAuthorities(AccessType accessType) {
        return accessType.name().equals("LEADER");
    }

    public List<Discipline> getDisciplines () {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);

        List<Discipline> disciplines = user.getDisciplines();
        Comparator<Discipline> comparator = Comparator.comparing(Discipline::getName);

        disciplines.sort(comparator);
        return disciplines;
    }
}