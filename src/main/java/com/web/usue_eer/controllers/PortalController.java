package com.web.usue_eer.controllers;

import com.web.usue_eer.entities.*;
import com.web.usue_eer.entities.enums.AccessType;
import com.web.usue_eer.payload.request.DisciplineRequest;
import com.web.usue_eer.security.services.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    private DisciplineAccessService disciplineAccessService;

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("disciplines", getDisciplines());
        return "index";
    }

    @GetMapping("/disciplines")
    public String disciplines(Model model) {
        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("content", "fragments/disciplines");
        return "index";
    }

    @GetMapping("/disciplines/create")
    public String createDisciplines(Model model) {
        List<Group> groups = groupService.findAllGroups();
        List<User> users = userDetailsService.findAllUsers();

        model.addAttribute("groups", groups);
        model.addAttribute("users", users);
        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("content", "fragments/create-discipline");
        return "index";
    }

    @PostMapping("/disciplines/create-getUserGroup")
    public ResponseEntity<List<User>> getUsersGroup(@RequestBody Group group) {
        List<User> users = userDetailsService.findUsersByGroupName(group.getName());
        return ResponseEntity.ok(users);
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
                user.addDiscipline(discipline);
                userDetailsService.saveUser(user);
                disciplineAccessService.saveAccess(new DisciplineAccess(user, discipline, access));
            }
        }

        User ownerUser = userDetailsService.findUserByUsername(owner);
        if (ownerUser != null) {
            ownerUser.addDiscipline(discipline);
            userDetailsService.saveUser(ownerUser);
            disciplineAccessService.saveAccess(new DisciplineAccess(ownerUser, discipline, AccessType.LEADER));
        }
        return ResponseEntity.ok(discipline);
    }

    @GetMapping("/discipline/{disciplineId}/{category}")
    public String getDisciplineCategoryContent(@PathVariable("disciplineId") Long disciplineId,
                                               @PathVariable("category") String category,
                                               Model model) {

        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("category", category);
        model.addAttribute("disciplineId", disciplineId);
        return "index";
    }

//    @GetMapping("/discipline/{disciplineId}/member-list")
//    public String getMemberList(Model model, @PathVariable String disciplineId) {
//        List<User> users = userDetailsService.findByDisciplinesId(Long.parseLong(disciplineId));
//        Map<String, List<User>> usersByGroup = users.stream()
//                .collect(Collectors.groupingBy(user -> user.getGroups().iterator().next().getName()));
//
//        model.addAttribute("usersByGroup", usersByGroup);
//        model.addAttribute("disciplines", getDisciplines());
//        model.addAttribute("content", "fragments/member-list");
//        return "index";
//    }

    @GetMapping("/discipline/{disciplineId}/member-list")
    public String getMemberList(Model model, @PathVariable String disciplineId) {
        List<User> users = userDetailsService.findByDisciplinesId(Long.parseLong(disciplineId));
        Map<String, List<User>> usersByGroup = users.stream()
                .collect(Collectors.groupingBy(user -> user.getGroups().iterator().next().getName()));

        model.addAttribute("usersByGroup", usersByGroup);
        model.addAttribute("disciplines", getDisciplines());

        users.forEach(user -> {
            AccessType accessType = disciplineAccessService.findByDisciplineIdAndUserId(Long.parseLong(disciplineId), user.getId()).getAccessType();
            user.setAccessType(accessType == AccessType.PARTICIPANT ? "Участник" : "Руководитель");
        });
        model.addAttribute("users", users);

        model.addAttribute("content", "fragments/member-list");
        return "index";
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