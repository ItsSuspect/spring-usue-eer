package com.web.usue_eer.controllers;

import com.web.usue_eer.entities.*;
import com.web.usue_eer.payload.request.GroupRequest;
import com.web.usue_eer.payload.request.SignUpRequest;
import com.web.usue_eer.payload.response.MessageResponse;
import com.web.usue_eer.security.services.FolderUserService;
import com.web.usue_eer.security.services.GroupService;
import com.web.usue_eer.security.services.RoleService;
import com.web.usue_eer.security.services.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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

    @Autowired
    private FolderUserService folderUserService;

    @GetMapping("/create")
    public String getSignUp() {
        return "admin-page";
    }

    @SuppressWarnings("squid:S2696")
    @PostMapping("/create-user")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (signUpRequest.getRole().equals("Выберите должность")) {
            return ResponseEntity.badRequest().body("Роль не выбрана");
        }

        if (signUpRequest.getRole().equals("ROLE_STUDENT") && !groupService.existsByName(signUpRequest.getGroup())) {
            return ResponseEntity.badRequest().body("Данной группы не существует");
        }

        if (userDetailsService.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Данное имя пользователя занято");
        }

        if (userDetailsService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Данная почта занята");
        }

        User user = new User(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getEmail(), signUpRequest.getName(), signUpRequest.getSurname(), signUpRequest.getMiddleName());

        Set<Role> roles = new HashSet<>();
        Role role = roleService.findRoleByName(signUpRequest.getRole());
        roles.add(role);

        user.setRoles(roles);

        Set<Group> groups = new HashSet<>();
        Group group = groupService.findGroupByName(signUpRequest.getGroup());
        groups.add(group);

        user.setGroups(groups);

        User createUser = userDetailsService.saveUser(user);

        FolderUser folderUser = new FolderUser();
        folderUser.setFolderName("Основная папка");
        folderUser.setUser(createUser);
        folderUser.setParentFolder(null);
        folderUserService.saveFolder(folderUser);

        return ResponseEntity.ok(new MessageResponse("Пользователь успешно зарегистрирован"));
    }

    @SuppressWarnings("squid:S2696")
    @PostMapping("/create-group")
    public ResponseEntity<?> createGroup(@Valid @RequestBody GroupRequest groupRequest) {
        if (groupService.existsByName(groupRequest.getName())) {
            return ResponseEntity.badRequest().body("Данная группа существует");
        }

        Group group = new Group(groupRequest.getName());
        groupService.saveGroup(group);
        return ResponseEntity.ok(new MessageResponse("Группа успешно создана"));
    }
}
