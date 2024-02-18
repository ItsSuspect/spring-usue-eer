package com.web.finance.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.web.finance.entities.Discipline;
import com.web.finance.entities.ERole;
import com.web.finance.entities.Role;
import com.web.finance.entities.User;
import com.web.finance.payload.request.SignUpRequest;
import com.web.finance.payload.response.MessageResponse;
import com.web.finance.repository.DisciplineRepository;
import jakarta.validation.Valid;

import com.web.finance.payload.request.SignInRequest;
import com.web.finance.payload.response.JwtResponse;
import com.web.finance.repository.RoleRepository;
import com.web.finance.repository.UserRepository;
import com.web.finance.security.jwt.JwtUtils;
import com.web.finance.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    DisciplineRepository disciplineRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @SuppressWarnings("squid:S2696")
    @PostMapping("/signIn")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignInRequest signInRequest) {
        if (signInRequest.getUsername() == null || signInRequest.getPassword() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: Неверное имя пользователя или пароль "));
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new JwtResponse(jwtCookie, userDetails.getId(),
                        userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: Данное имя пользователя занято"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: Данная почта занята"));
        }

        User user = new User(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()), signUpRequest.getEmail(), signUpRequest.getFullName());

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT).orElseThrow(() -> new RuntimeException("Ошибка: Роль не найдена"));
        roles.add(userRole);
//
//        Set<Discipline> disciplines = new HashSet<>();
//        Discipline discipline = disciplineRepository.findByName("Разработка многоуровневых приложений").get();
//        disciplines.add(discipline);

        user.setRoles(roles);
//        user.setDisciplines(disciplines);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Пользователь успешно зарегистрирован"));
    }
}
