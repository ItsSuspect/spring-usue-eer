package com.web.usue_eer.controllers.DisciplineCategory;

import com.web.usue_eer.entities.FileSharing;
import com.web.usue_eer.entities.User;
import com.web.usue_eer.entities.UserDiscipline;
import com.web.usue_eer.entities.enums.AccessType;
import com.web.usue_eer.payload.response.FileSharingResponse;
import com.web.usue_eer.payload.response.FileSharingUserResponse;
import com.web.usue_eer.security.services.DisciplineService;
import com.web.usue_eer.security.services.FileSharingService;
import com.web.usue_eer.security.services.UserDetailsServiceImpl;
import com.web.usue_eer.security.services.UserDisciplineService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/portal/discipline")
public class FileSharingController {
    private final FileSharingService fileSharingService;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserDisciplineService userDisciplineService;
    private final DisciplineService disciplineService;

    @Autowired
    public FileSharingController(FileSharingService fileSharingService, UserDetailsServiceImpl userDetailsService, UserDisciplineService userDisciplineService, DisciplineService disciplineService) {
        this.fileSharingService = fileSharingService;
        this.userDetailsService = userDetailsService;
        this.userDisciplineService = userDisciplineService;
        this.disciplineService = disciplineService;
    }

    @GetMapping("/{disciplineId}/file-sharing")
    @Transactional
    public String getFileSharing(Model model, @PathVariable Long disciplineId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);

        boolean authorities = getAuthorities(userDisciplineService.findByDisciplineIdAndUserId(disciplineId, user.getId()).getAccessType());
        if (authorities) {
            List<UserDiscipline> userDisciplines = userDisciplineService.findUserDisciplinesParticipantByDisciplineId(disciplineId);
            List<FileSharing> fileSharings = fileSharingService.findFileSharingsByDisciplineId(disciplineId);

            List<FileSharingUserResponse> usersWithFiles = new ArrayList<>();
            List<FileSharingResponse> fileSharingResponses = new ArrayList<>();

            Set<Long> userIdsWithFiles = new HashSet<>();

            for (FileSharing fileSharing : fileSharings) {
                userIdsWithFiles.add(fileSharing.getUser().getId());
                fileSharingResponses.add(new FileSharingResponse(
                        fileSharing.getUser().getId(),
                        fileSharing.getId(),
                        fileSharing.getFileName(),
                        fileSharing.getDateAdd()
                ));
            }

            for (UserDiscipline userDiscipline : userDisciplines) {
                if (userIdsWithFiles.contains(userDiscipline.getUser().getId())) {
                    usersWithFiles.add(new FileSharingUserResponse(
                            userDiscipline.getUser().getId(),
                            userDiscipline.getUser().getSurname() + ' ' + userDiscipline.getUser().getName() + ' ' + userDiscipline.getUser().getMiddleName()
                    ));
                }
            }

            model.addAttribute("folders", usersWithFiles);
            model.addAttribute("files", fileSharingResponses);
            model.addAttribute("content", "discipline-tabs/discipline-file-sharing/discipline-file-sharing-teacher");
        }
        else {
            List<FileSharing> fileSharings = fileSharingService.findFileSharingsByUserIdAndDisciplineId(user.getId(), disciplineId);
            List<FileSharingResponse> fileSharingResponses = fileSharings.stream()
                            .map(fileSharing -> new FileSharingResponse(
                                    fileSharing.getId(),
                                    fileSharing.getFileName(),
                                    fileSharing.getDateAdd()
                            )).collect(Collectors.toList());
            model.addAttribute("files", fileSharingResponses);
            model.addAttribute("content", "discipline-tabs/discipline-file-sharing/discipline-file-sharing-student");
        }
        return "index";
    }

    @PostMapping("/{disciplineId}/file-sharing/save-file")
    public ResponseEntity<Void> saveFileSharing(@RequestParam("file") MultipartFile file, @PathVariable Long disciplineId) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userDetailsService.findUserByUsername(username);

            FileSharing fileSharing = new FileSharing();
            fileSharing.setFileName(file.getOriginalFilename());
            fileSharing.setFileData(file.getBytes());
            fileSharing.setFileType(file.getContentType());
            fileSharing.setFileSize(file.getSize());
            fileSharing.setDateAdd(LocalDateTime.now().withSecond(0).withNano(0).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            fileSharing.setDiscipline(disciplineService.findDisciplineById(disciplineId));
            fileSharing.setUser(user);

            fileSharingService.saveFile(fileSharing);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{disciplineId}/file-sharing/delete-file")
    public ResponseEntity<Void> deleteFileSharing(@RequestParam("fileId") Long fileId, @PathVariable Long disciplineId) {
        try {
            fileSharingService.deleteFileSharingById(fileId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public boolean getAuthorities(AccessType accessType) {
        return accessType.name().equals("LEADER");
    }
}
