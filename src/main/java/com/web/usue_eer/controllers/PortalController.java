package com.web.usue_eer.controllers;

import com.web.usue_eer.entities.*;
import com.web.usue_eer.entities.enums.AccessType;
import com.web.usue_eer.payload.request.*;
import com.web.usue_eer.payload.response.UserResponse;
import com.web.usue_eer.security.services.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/portal")
public class PortalController {
    private final UserDetailsServiceImpl userDetailsService;
    private final GroupService groupService;
    private final DisciplineService disciplineService;
    private final UserDisciplineService userDisciplineService;
    private final AdvertisementService advertisementService;
    private final UserNotificationService userNotificationService;
    private final InformationService informationService;
    private final FolderDisciplineService folderDisciplineService;
    private final FileDisciplineService fileDisciplineService;

    @Autowired
    public PortalController(UserDetailsServiceImpl userDetailsService, GroupService groupService, DisciplineService disciplineService, UserDisciplineService userDisciplineService, AdvertisementService advertisementService, UserNotificationService userNotificationService, InformationService informationService, FolderDisciplineService folderDisciplineService, FileDisciplineService fileDisciplineService) {
        this.userDetailsService = userDetailsService;
        this.groupService = groupService;
        this.disciplineService = disciplineService;
        this.userDisciplineService = userDisciplineService;
        this.advertisementService = advertisementService;
        this.userNotificationService = userNotificationService;
        this.informationService = informationService;
        this.folderDisciplineService = folderDisciplineService;
        this.fileDisciplineService = fileDisciplineService;
    }


    @GetMapping("")
    public String index(Model model) {
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

        model.addAttribute("groups", groups);
        model.addAttribute("users", userResponses);
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
        User ownerUser = userDetailsService.findUserByUsername(owner);

        Discipline discipline = disciplineService.saveDiscipline(new Discipline(disciplineRequest.getName(), ownerUser));
        Map<String, String> usersWithAccess = disciplineRequest.getUsers();

        for (Map.Entry<String, String> entry : usersWithAccess.entrySet()) {
            String username = entry.getKey();
            AccessType access = AccessType.valueOf(entry.getValue());

            User user = userDetailsService.findUserByUsername(username);
            if (user != null) {
                userDisciplineService.saveAccess(new UserDiscipline(user, discipline, access));
            }
        }

        if (ownerUser != null) {
            userDisciplineService.saveAccess(new UserDiscipline(ownerUser, discipline, AccessType.LEADER));
        }

        FolderDiscipline folderDiscipline = new FolderDiscipline();
        folderDiscipline.setFolderName("Основная папка");
        folderDiscipline.setDiscipline(discipline);
        folderDiscipline.setParentFolder(null);
        folderDisciplineService.saveFolder(folderDiscipline);

        return ResponseEntity.ok(discipline);
    }

    @GetMapping("/discipline/{disciplineId}/{category}")
    public String getDisciplineCategoryContent(@PathVariable Long disciplineId, @PathVariable String category, Model model) {
        model.addAttribute("category", category);
        model.addAttribute("disciplineId", disciplineId);
        return "index";
    }

    @GetMapping("/discipline/{disciplineId}/member-list")
    public String getMemberList(Model model, @PathVariable Long disciplineId) {
        List<User> users = userDetailsService.findByDisciplinesId(disciplineId);
        Map<String, List<User>> usersByGroup = users.stream()
                .collect(Collectors.groupingBy(user -> user.getGroups().iterator().next().getName()));

        users.forEach(user -> {
            AccessType accessType = userDisciplineService.findByDisciplineIdAndUserId(disciplineId, user.getId()).getAccessType();
            user.setAccessType(accessType == AccessType.PARTICIPANT ? "Участник" : "Руководитель");
        });

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);

        UserDiscipline userDiscipline = userDisciplineService.findByDisciplineIdAndUserId(disciplineId, user.getId());

        model.addAttribute("usersByGroup", usersByGroup);
        model.addAttribute("users", users);
        model.addAttribute("authorities", getAuthorities(userDiscipline.getAccessType()));
        model.addAttribute("content", "fragments/discipline-members");
        return "index";
    }

    @GetMapping("/discipline/{disciplineId}/information")
    public String getDisciplineInformation(Model model, @PathVariable Long disciplineId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);

        UserDiscipline userDiscipline = userDisciplineService.findByDisciplineIdAndUserId(disciplineId, user.getId());

        Information information = informationService.findInformationByDisciplineId(disciplineId)
                .orElseGet(() -> {
                    Information defaultInformation = new Information();
                    defaultInformation.setInformationOfDiscipline("Преподаватель скоро предоставит информацию");
                    defaultInformation.setInformationOfTeacher("Преподаватель скоро предоставит информацию");
                    defaultInformation.setContacts("Преподаватель скоро предоставит информацию");
                    return defaultInformation;
                });

        model.addAttribute("information", information);
        model.addAttribute("authorities", getAuthorities(userDiscipline.getAccessType()));
        model.addAttribute("content", "fragments/discipline-information");
        return "index";
    }

    @GetMapping("/discipline/{disciplineId}/advertisements")
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
        model.addAttribute("content", "fragments/advertisements");
        return "index";
    }

    @GetMapping("/discipline/{disciplineId}/advertisements/{advertisementId}/edit-advertisement")
    public String getEditDisciplineAdvertisements(Model model, @PathVariable Long disciplineId, @PathVariable Long advertisementId) {
        Advertisement advertisement = advertisementService.findAdvertisementById(advertisementId);

        model.addAttribute("advertisement", advertisement);
        model.addAttribute("content", "fragments/edit-advertisement");
        return "index";
    }

    @PostMapping("/discipline/{disciplineId}/advertisements/{advertisementId}/edit-advertisement")
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

    @GetMapping("/discipline/{disciplineId}/advertisements/create-advertisement")
    public String getCreateAdvertisement(Model model, @PathVariable Long disciplineId) {
        model.addAttribute("content", "fragments/create-advertisement");
        return "index";
    }

    @PostMapping("/discipline/{disciplineId}/advertisements/create-advertisement")
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

    @GetMapping("/discipline/{disciplineId}/information/edit-information")
    public String getEditInformation(Model model, @PathVariable Long disciplineId) {
        Optional<Information> informationOptional = informationService.findInformationByDisciplineId(disciplineId);
        Information information = informationOptional.orElse(new Information());

        model.addAttribute("information", information);
        model.addAttribute("content", "fragments/enter-basic-information");
        return "index";
    }

    @PostMapping("/discipline/{disciplineId}/information/edit-information")
    public ResponseEntity<Void> editInformation(@PathVariable Long disciplineId, @RequestBody InformationRequest informationRequest) {
        Discipline discipline = disciplineService.findDisciplineById(disciplineId);
        Information information = new Information(
                informationRequest.getInformationOfDiscipline(),
                informationRequest.getInformationOfTeacher(),
                informationRequest.getContacts(),
                discipline
        );

        if (informationRequest.getId() != null) {
            information.setId(informationRequest.getId());
        }

        informationService.saveInformation(information);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/discipline/{disciplineId}/resources")
    @Transactional
    public String getResources(Model model, @PathVariable Long disciplineId) {
        List<FolderDiscipline> folderDisciplines = folderDisciplineService.findFolderDisciplinesByDisciplineId(disciplineId);
        List<FileDiscipline> fileDisciplines = fileDisciplineService.findFileDisciplinesByDisciplineId(disciplineId);

        model.addAttribute("folders", folderDisciplines);
        model.addAttribute("files", fileDisciplines);
        model.addAttribute("content", "fragments/discipline-resources");
        return "index";
    }

    @GetMapping("/discipline/{disciplineId}/resources/download/{fileId}")
    @Transactional
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Long disciplineId, @PathVariable Long fileId) {
        FileDiscipline file = fileDisciplineService.findFileDisciplineById(fileId);

        ByteArrayResource resource = new ByteArrayResource(file.getFileData());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
                new String(file.getFileName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1) + "\"; charset=UTF-8");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(file.getFileSize())
                .body(resource);
    }

    @PostMapping("/discipline/{disciplineId}/resources/save-folder")
    public ResponseEntity<Void> saveFolder(@PathVariable Long disciplineId, @RequestBody FolderRequest folderRequest) {
        FolderDiscipline folderDiscipline = new FolderDiscipline();
        folderDiscipline.setFolderName(folderRequest.getName());
        folderDiscipline.setParentFolder(folderDisciplineService.findFolderDisciplineById(folderRequest.getParentFolder()));
        folderDiscipline.setDiscipline(disciplineService.findDisciplineById(disciplineId));
        folderDiscipline.setDateAdd(LocalDateTime.now().withSecond(0).withNano(0).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        folderDisciplineService.saveFolder(folderDiscipline);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/discipline/{disciplineId}/resources/save-file")
    public ResponseEntity<Void> saveFile(@PathVariable Long disciplineId, @RequestParam("file") MultipartFile file, @RequestParam("parentFolder") Long parentFolderId) {
        try {
            String fileName = file.getOriginalFilename();
            byte[] fileData = file.getBytes();
            String fileType = file.getContentType();
            long fileSize = file.getSize();

            FileDiscipline fileDiscipline = new FileDiscipline();
            fileDiscipline.setFileName(fileName);
            fileDiscipline.setFileData(fileData);
            fileDiscipline.setFileType(fileType);
            fileDiscipline.setFileSize(fileSize);
            fileDiscipline.setDateAdd(LocalDateTime.now().withSecond(0).withNano(0).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            fileDiscipline.setDiscipline(disciplineService.findDisciplineById(disciplineId));
            fileDiscipline.setFolder(folderDisciplineService.findFolderDisciplineById(parentFolderId));

            fileDisciplineService.saveFile(fileDiscipline);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/discipline/{disciplineId}/resources/delete-file")
    public ResponseEntity<Void> deleteFile(@PathVariable Long disciplineId, @RequestParam("fileId") Long fileId) {
        try {
            fileDisciplineService.deleteFileDisciplineById(fileId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/discipline/{disciplineId}/resources/delete-folder")
    public ResponseEntity<Void> deleteFolder(@PathVariable Long disciplineId, @RequestParam("folderId") Long folderId) {
        try {
            List<FileDiscipline> fileDisciplines = fileDisciplineService.findFileDisciplinesByFolderId(folderId);
            for (FileDiscipline file : fileDisciplines) {
                fileDisciplineService.deleteFileDisciplineById(file.getId());
            }
            folderDisciplineService.deleteFolderDisciplineById(folderId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

//    @GetMapping("/discipline/{disciplineId}/edit-members")
//    public String getResources1(Model model, @PathVariable Long disciplineId) {
//        model.addAttribute("content", "fragments/edit-members");
//        return "index";
//    }

    public boolean getAuthorities(AccessType accessType) {
        return accessType.name().equals("LEADER");
    }
}