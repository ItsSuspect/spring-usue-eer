package com.web.usue_eer.controllers;

import com.web.usue_eer.entities.Group;
import com.web.usue_eer.entities.Role;
import com.web.usue_eer.entities.User;
import com.web.usue_eer.payload.request.SignUpRequest;
import com.web.usue_eer.payload.response.MessageResponse;
import com.web.usue_eer.repository.DisciplineRepository;
import com.web.usue_eer.repository.GroupRepository;
import com.web.usue_eer.repository.RoleRepository;
import com.web.usue_eer.repository.UserRepository;
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

    @GetMapping("/signUp")
    public String getSignUp() {
        return "registration";
    }
    @PostMapping("/signUp")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userDetailsService.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: Данное имя пользователя занято"));
        }

        if (userDetailsService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: Данная почта занята"));
        }

        User user = new User(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()), signUpRequest.getEmail(), signUpRequest.getFullName());

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
}
