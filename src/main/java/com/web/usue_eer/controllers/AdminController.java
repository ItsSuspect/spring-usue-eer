package com.web.usue_eer.controllers;

import com.web.usue_eer.entities.Group;
import com.web.usue_eer.entities.Role;
import com.web.usue_eer.entities.User;
import com.web.usue_eer.payload.request.GroupRequest;
import com.web.usue_eer.payload.request.SignUpRequest;
import com.web.usue_eer.payload.response.MessageResponse;
import com.web.usue_eer.security.services.GroupService;
import com.web.usue_eer.security.services.RoleService;
import com.web.usue_eer.security.services.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    RoleService roleService;

    @Autowired
    GroupService groupService;

    @Autowired
    PasswordEncoder encoder;

    @GetMapping("/create")
    public String getSignUp() {
        return "admin-page";
    }

    @SuppressWarnings("squid:S2696")
    @PostMapping("/create-user")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (signUpRequest.getRole().equals("Выберите должность")) {
            return ResponseEntity.badRequest().body(new MessageResponse("Роль не выбрана"));
        }

        if (signUpRequest.getUsername().isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Логин не должен быть пустым. Заполните для этого ФИО."));
        }

        if (signUpRequest.getName().isEmpty() || signUpRequest.getSurname().isEmpty() || signUpRequest.getMiddleName().isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Имя, фамилия или отчество пустое"));
        }

        if (signUpRequest.getRole().equals("ROLE_STUDENT") && !groupService.existsByName(signUpRequest.getGroup())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Данной группы не существует"));
        }

        if (userDetailsService.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Данное имя пользователя занято"));
        }

        if (userDetailsService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Данная почта занята"));
        }

        User user = new User(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getEmail(), signUpRequest.getName(), signUpRequest.getSurname(), signUpRequest.getMiddleName());

        Set<Role> roles = new HashSet<>();
        Role role = roleService.findRoleByName(signUpRequest.getRole());
        roles.add(role);

        user.setRoles(roles);

        if (signUpRequest.getRole().equals("ROLE_STUDENT")) {
            Set<Group> groups = new HashSet<>();
            Group group = groupService.findGroupByName(signUpRequest.getGroup());
            groups.add(group);

            user.setGroups(groups);
        }

        userDetailsService.saveUser(user);
        return ResponseEntity.ok(new MessageResponse("Пользователь успешно зарегистрирован"));
    }

    @SuppressWarnings("squid:S2696")
    @PostMapping("/create-group")
    public ResponseEntity<?> createGroup(@Valid @RequestBody GroupRequest groupRequest) {
        if (groupRequest.getName().isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Название группы пустое"));
        }

        if (groupService.existsByName(groupRequest.getName())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Данная группа существует"));
        }

        Group group = new Group(groupRequest.getName());
        groupService.saveGroup(group);
        return ResponseEntity.ok(new MessageResponse("Группа успешно создана"));
    }
}
