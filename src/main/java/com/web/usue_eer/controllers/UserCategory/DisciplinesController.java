package com.web.usue_eer.controllers.UserCategory;

import com.web.usue_eer.entities.*;
import com.web.usue_eer.entities.enums.AccessType;
import com.web.usue_eer.payload.request.DisciplineRequest;
import com.web.usue_eer.payload.response.UserResponse;
import com.web.usue_eer.security.services.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/portal")
public class DisciplinesController {
    private final UserDetailsServiceImpl userDetailsService;
    private final GroupService groupService;
    private final DisciplineService disciplineService;
    private final UserDisciplineService userDisciplineService;
    private final FolderDisciplineService folderDisciplineService;
    private final RequestDisciplineService requestDisciplineService;

    @Autowired
    public DisciplinesController(UserDetailsServiceImpl userDetailsService, GroupService groupService, DisciplineService disciplineService, UserDisciplineService userDisciplineService, FolderDisciplineService folderDisciplineService, RequestDisciplineService requestDisciplineService) {
        this.userDetailsService = userDetailsService;
        this.groupService = groupService;
        this.disciplineService = disciplineService;
        this.userDisciplineService = userDisciplineService;
        this.folderDisciplineService = folderDisciplineService;
        this.requestDisciplineService = requestDisciplineService;
    }

    @GetMapping("/disciplines")
    public String disciplines(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);
        List<Discipline> userDisciplines = user.getDisciplines();

        List<Discipline> openDisciplines = disciplineService.findDisciplinesByAccess(true);
        openDisciplines.removeAll(userDisciplines);
        model.addAttribute("openDisciplines", openDisciplines);
        model.addAttribute("content", "top-navigation/user-disciplines/user-disciplines");
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
        model.addAttribute("content", "top-navigation/user-disciplines/create-discipline");
        return "index";
    }

    @PostMapping("/disciplines/create/getUserGroup")
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

        Discipline discipline = new Discipline(disciplineRequest.getName(), ownerUser, disciplineRequest.isAccess());
        disciplineService.saveDiscipline(discipline);
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

    @PostMapping("/disciplines/confirm-request/{disciplineId}")
    public ResponseEntity<Void> confirmRequest(@PathVariable Long disciplineId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);
        Discipline discipline = disciplineService.findDisciplineById(disciplineId);

        if (userDisciplineService.existsByDisciplineIdAndUserId(discipline.getId(), user.getId())) {
            return ResponseEntity.badRequest().build();
        }

        if (requestDisciplineService.existsRequestDisciplineByUserIdAndDisciplineId(user.getId(), discipline.getId())) {
            return ResponseEntity.badRequest().build();
        }

        RequestDiscipline requestDiscipline = new RequestDiscipline();
        requestDiscipline.setDiscipline(discipline);
        requestDiscipline.setUser(user);

        requestDisciplineService.saveRequestDiscipline(requestDiscipline);
        return ResponseEntity.ok().build();
    }
}
