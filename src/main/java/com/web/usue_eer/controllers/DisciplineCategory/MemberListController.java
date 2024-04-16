package com.web.usue_eer.controllers.DisciplineCategory;

import com.web.usue_eer.entities.*;
import com.web.usue_eer.entities.enums.AccessType;
import com.web.usue_eer.payload.request.DisciplineRequest;
import com.web.usue_eer.payload.response.UserResponse;
import com.web.usue_eer.security.services.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/portal/discipline")
public class MemberListController {
    private final UserDetailsServiceImpl userDetailsService;
    private final UserDisciplineService userDisciplineService;
    private final DisciplineService disciplineService;
    private final RequestDisciplineService requestDisciplineService;
    private final GroupService groupService;

    @Autowired
    public MemberListController(UserDetailsServiceImpl userDetailsService, UserDisciplineService userDisciplineService, DisciplineService disciplineService, RequestDisciplineService requestDisciplineService, GroupService groupService) {
        this.userDetailsService = userDetailsService;
        this.userDisciplineService = userDisciplineService;
        this.disciplineService = disciplineService;
        this.requestDisciplineService = requestDisciplineService;
        this.groupService = groupService;
    }

    @GetMapping("/{disciplineId}/member-list")
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

        Discipline discipline = disciplineService.findDisciplineById(disciplineId);
        model.addAttribute("access", discipline.isAccess());
        model.addAttribute("usersByGroup", usersByGroup);
        model.addAttribute("authorities", getAuthorities(userDiscipline.getAccessType()));
        model.addAttribute("content", "discipline-tabs/discipline-members/discipline-members");
        return "index";
    }

    @GetMapping("/{disciplineId}/member-list/requests")
    public String getRequests(Model model, @PathVariable Long disciplineId) {
        List<RequestDiscipline> requestDisciplines = requestDisciplineService.findRequestDisciplinesByDisciplineId(disciplineId);

        List<User> users = requestDisciplines.stream()
                .map(RequestDiscipline::getUser)
                .collect(Collectors.toList());

        Map<String, List<User>> usersByGroup = users.stream()
                .collect(Collectors.groupingBy(user -> user.getGroups().iterator().next().getName()));

        model.addAttribute("usersByGroup", usersByGroup);
        model.addAttribute("content", "discipline-tabs/discipline-members/discipline-requests");
        return "index";
    }

    @PostMapping("/{disciplineId}/member-list/requests/acceptUser/{userId}")
    public ResponseEntity<Void> postAcceptUser(@PathVariable Long disciplineId, @PathVariable Long userId, @RequestParam("accessType") String accessType) {
        User user = userDetailsService.findById(userId);
        Discipline discipline = disciplineService.findDisciplineById(disciplineId);

        UserDiscipline userDiscipline = new UserDiscipline(user, discipline, AccessType.valueOf(accessType));
        userDisciplineService.saveAccess(userDiscipline);
        requestDisciplineService.deleteByDisciplineIdAndUserId(disciplineId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{disciplineId}/member-list/requests/declineUser/{userId}")
    public ResponseEntity<Void> postDeclineUser(@PathVariable Long disciplineId, @PathVariable Long userId) {
        requestDisciplineService.deleteByDisciplineIdAndUserId(disciplineId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{disciplineId}/member-list/edit-members")
    public String getEditMemberList(Model model, @PathVariable Long disciplineId) {
        List<Group> groupsAll = groupService.findAllGroups();
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

        model.addAttribute("groupsAll", groupsAll);
        model.addAttribute("usersAll", userResponses);

        List<User> usersInDiscipline = userDetailsService.findByDisciplinesId(disciplineId);
        Map<String, List<User>> usersByGroup = usersInDiscipline.stream()
                .collect(Collectors.groupingBy(user -> user.getGroups().iterator().next().getName()));

        usersInDiscipline.forEach(user -> {
            AccessType accessType = userDisciplineService.findByDisciplineIdAndUserId(disciplineId, user.getId()).getAccessType();
            user.setAccessType(accessType == AccessType.PARTICIPANT ? "Участник" : "Руководитель");
        });

        model.addAttribute("usersByGroup", usersByGroup);
        model.addAttribute("usersInDiscipline", usersInDiscipline);
        model.addAttribute("content", "discipline-tabs/discipline-members/edit-members");
        return "index";
    }

    @PostMapping("/{disciplineId}/member-list/edit-members")
    @Transactional
    public ResponseEntity<Void> postEditMemberList(@PathVariable Long disciplineId, @RequestBody DisciplineRequest disciplineRequest) {
        Discipline discipline = disciplineService.findDisciplineById(disciplineId);
        Map<String, String> usersWithAccess = disciplineRequest.getUsers();

        List<Long> userIdsToKeep = new ArrayList<>();

        for (Map.Entry<String, String> entry : usersWithAccess.entrySet()) {
            String username = entry.getKey();
            AccessType access = AccessType.valueOf(entry.getValue());

            User user = userDetailsService.findUserByUsername(username);
            if (user != null && user != discipline.getOwner()) {
                UserDiscipline userDiscipline = userDisciplineService.findByDisciplineIdAndUserId(disciplineId, user.getId());
                if (userDiscipline != null && !(userDiscipline.getUser().getUsername().equals(discipline.getOwner().getUsername()))) {
                    userDiscipline.setAccessType(access);
                    userIdsToKeep.add(user.getId());
                } else {
                    userDisciplineService.saveAccess(new UserDiscipline(user, discipline, access));
                    userIdsToKeep.add(user.getId());
                }
            }
        }

        List<UserDiscipline> userDisciplines = userDisciplineService.findUserDisciplinesByDisciplineId(disciplineId);
        for (UserDiscipline userDiscipline : userDisciplines) {
            if (!(userIdsToKeep.contains(userDiscipline.getUser().getId())) && !userDiscipline.getUser().getUsername().equals(discipline.getOwner().getUsername())) {
                userDisciplineService.deleteById(userDiscipline.getId());
            }
        }

        return ResponseEntity.ok().build();
    }

    public boolean getAuthorities(AccessType accessType) {
        return accessType.name().equals("LEADER");
    }
}
