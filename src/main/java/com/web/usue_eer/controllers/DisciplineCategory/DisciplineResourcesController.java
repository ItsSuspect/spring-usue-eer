package com.web.usue_eer.controllers.DisciplineCategory;

import com.web.usue_eer.entities.FileDiscipline;
import com.web.usue_eer.entities.FolderDiscipline;
import com.web.usue_eer.entities.User;
import com.web.usue_eer.entities.UserDiscipline;
import com.web.usue_eer.entities.enums.AccessType;
import com.web.usue_eer.payload.request.FolderRequest;
import com.web.usue_eer.security.services.*;
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
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/portal/discipline")
public class DisciplineResourcesController {
    private final FolderDisciplineService folderDisciplineService;
    private final FileDisciplineService fileDisciplineService;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserDisciplineService userDisciplineService;
    private final DisciplineService disciplineService;

    @Autowired
    public DisciplineResourcesController(FolderDisciplineService folderDisciplineService, FileDisciplineService fileDisciplineService, UserDetailsServiceImpl userDetailsService, UserDisciplineService userDisciplineService, DisciplineService disciplineService) {
        this.folderDisciplineService = folderDisciplineService;
        this.fileDisciplineService = fileDisciplineService;
        this.userDetailsService = userDetailsService;
        this.userDisciplineService = userDisciplineService;
        this.disciplineService = disciplineService;
    }

    @GetMapping("/{disciplineId}/resources")
    @Transactional
    public String getDisciplineResources(Model model, @PathVariable Long disciplineId) {
        List<FolderDiscipline> folderDisciplines = folderDisciplineService.findFolderDisciplinesByDisciplineId(disciplineId);
        List<FileDiscipline> fileDisciplines = fileDisciplineService.findFileDisciplinesByDisciplineId(disciplineId);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);
        UserDiscipline userDiscipline = userDisciplineService.findByDisciplineIdAndUserId(disciplineId, user.getId());

        model.addAttribute("authorities", getAuthorities(userDiscipline.getAccessType()));
        model.addAttribute("folders", folderDisciplines);
        model.addAttribute("files", fileDisciplines);
        model.addAttribute("content", "discipline-tabs/discipline-resources/discipline-resources");
        return "index";
    }

    @PostMapping("/{disciplineId}/resources/save-folder")
    public ResponseEntity<Void> saveFolder(@PathVariable Long disciplineId, @RequestBody FolderRequest folderRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);
        String author = user.getSurname() + ' ' + user.getName().charAt(0) + '.' + user.getMiddleName().charAt(0);

        FolderDiscipline folderDiscipline = new FolderDiscipline();
        folderDiscipline.setFolderName(folderRequest.getName());
        folderDiscipline.setParentFolder(folderDisciplineService.findFolderDisciplineById(folderRequest.getParentFolder()));
        folderDiscipline.setDiscipline(disciplineService.findDisciplineById(disciplineId));
        folderDiscipline.setDateAdd(LocalDateTime.now().withSecond(0).withNano(0).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        folderDiscipline.setAuthor(author);
        folderDisciplineService.saveFolder(folderDiscipline);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{disciplineId}/resources/save-file")
    public ResponseEntity<Void> saveFile(@PathVariable Long disciplineId, @RequestParam("file") MultipartFile file, @RequestParam("parentFolder") Long parentFolderId) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userDetailsService.findUserByUsername(username);
            String author = user.getSurname() + ' ' + user.getName().charAt(0) + '.' + user.getMiddleName().charAt(0);

            FileDiscipline fileDiscipline = new FileDiscipline();
            fileDiscipline.setFileName(file.getOriginalFilename());
            fileDiscipline.setFileData(file.getBytes());
            fileDiscipline.setFileType(file.getContentType());
            fileDiscipline.setFileSize(file.getSize());
            fileDiscipline.setDateAdd(LocalDateTime.now().withSecond(0).withNano(0).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            fileDiscipline.setAuthor(author);
            fileDiscipline.setDiscipline(disciplineService.findDisciplineById(disciplineId));
            fileDiscipline.setFolder(folderDisciplineService.findFolderDisciplineById(parentFolderId));

            fileDisciplineService.saveFile(fileDiscipline);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{disciplineId}/resources/delete-file")
    public ResponseEntity<Void> deleteFile(@PathVariable Long disciplineId, @RequestParam("fileId") Long fileId) {
        try {
            fileDisciplineService.deleteFileDisciplineById(fileId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{disciplineId}/resources/delete-folder")
    public ResponseEntity<Void> deleteFolder(@PathVariable Long disciplineId, @RequestParam("folderId") Long folderId) {
        try {
            List<FileDiscipline> fileDisciplines = fileDisciplineService.findFileDisciplinesByFolderId(folderId);
            List<FolderDiscipline> subFolders = folderDisciplineService.findAllSubFolders(folderId);
            subFolders.sort(Comparator.comparing(FolderDiscipline::getId).reversed());

            for (FileDiscipline file : fileDisciplines) {
                fileDisciplineService.deleteFileDisciplineById(file.getId());
            }

            for (FolderDiscipline subFolder : subFolders) {
                for (FileDiscipline fileDiscipline : fileDisciplineService.findFileDisciplinesByFolderId(subFolder.getId())) {
                    fileDisciplineService.deleteFileDisciplineById(fileDiscipline.getId());
                }
                folderDisciplineService.deleteFolderDisciplineById(subFolder.getId());
            }

            folderDisciplineService.deleteFolderDisciplineById(folderId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public boolean getAuthorities(AccessType accessType) {
        return accessType.name().equals("LEADER");
    }
}
