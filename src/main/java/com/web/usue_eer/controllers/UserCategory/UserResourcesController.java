package com.web.usue_eer.controllers.UserCategory;

import com.web.usue_eer.entities.FileUser;
import com.web.usue_eer.entities.FolderUser;
import com.web.usue_eer.entities.User;
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
@RequestMapping("/portal")
public class UserResourcesController {
    private final FolderUserService folderUserService;
    private final FileUserService fileUserService;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public UserResourcesController(FolderUserService folderUserService, FileUserService fileUserService, UserDetailsServiceImpl userDetailsService) {
        this.folderUserService = folderUserService;
        this.fileUserService = fileUserService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/resources")
    @Transactional
    public String getUserResources(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);

        List<FolderUser> folderUsers = folderUserService.findFolderUsersByUserId(user.getId());
        List<FileUser> fileUsers = fileUserService.findFileUsersByUserId(user.getId());

        model.addAttribute("folders", folderUsers);
        model.addAttribute("files", fileUsers);
        model.addAttribute("content", "top-navigation/user-resources/user-resources");
        return "index";
    }

    @PostMapping("/resources/save-folder")
    public ResponseEntity<Void> saveUserFolder(@RequestBody FolderRequest folderRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);

        FolderUser folderUser = new FolderUser();
        folderUser.setFolderName(folderRequest.getName());
        folderUser.setParentFolder(folderUserService.findFolderUserById(folderRequest.getParentFolder()));
        folderUser.setUser(user);
        folderUser.setDateAdd(LocalDateTime.now().withSecond(0).withNano(0).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        folderUserService.saveFolder(folderUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resources/save-file")
    public ResponseEntity<Void> saveUserFile(@RequestParam("file") MultipartFile file, @RequestParam("parentFolder") Long parentFolderId) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userDetailsService.findUserByUsername(username);

            FileUser fileUser = new FileUser();
            fileUser.setFileName(file.getOriginalFilename());
            fileUser.setFileData(file.getBytes());
            fileUser.setFileType(file.getContentType());
            fileUser.setFileSize(file.getSize());
            fileUser.setDateAdd(LocalDateTime.now().withSecond(0).withNano(0).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            fileUser.setUser(user);
            fileUser.setFolder(folderUserService.findFolderUserById(parentFolderId));

            fileUserService.saveFile(fileUser);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/resources/delete-file")
    public ResponseEntity<Void> deleteUserFile(@RequestParam("fileId") Long fileId) {
        try {
            fileUserService.deleteFileUserById(fileId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/resources/delete-folder")
    public ResponseEntity<Void> deleteUserFolder(@RequestParam("folderId") Long folderId) {
        try {
            List<FileUser> fileUsers = fileUserService.findFileUsersByFolderId(folderId);
            List<FolderUser> subFolders = folderUserService.findAllSubFolders(folderId);
            subFolders.sort(Comparator.comparing(FolderUser::getId).reversed());

            for (FileUser file : fileUsers) {
                fileUserService.deleteFileUserById(file.getId());
            }

            for (FolderUser subFolder : subFolders) {
                for (FileUser fileUser : fileUserService.findFileUsersByFolderId(subFolder.getId())) {
                    fileUserService.deleteFileUserById(fileUser.getId());
                }
                folderUserService.deleteFolderUserById(subFolder.getId());
            }

            folderUserService.deleteFolderUserById(folderId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}