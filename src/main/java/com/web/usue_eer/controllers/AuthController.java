package com.web.usue_eer.controllers;

import com.web.usue_eer.payload.response.MessageResponse;
import jakarta.validation.Valid;

import com.web.usue_eer.payload.request.SignInRequest;
import com.web.usue_eer.payload.response.JwtResponse;
import com.web.usue_eer.security.jwt.JwtUtils;
import com.web.usue_eer.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/signIn")
    public String getSignIn() {
        return "authorization";
    }

    @GetMapping("/signUp")
    public String getSignUp() {
        return "registration";
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignInRequest signInRequest) {
        if (signInRequest.getUsername() == null || signInRequest.getPassword() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка: Неверное имя пользователя или пароль "));
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie;
        if (signInRequest.isRememberMe()) jwtCookie = jwtUtils.generateJwtCookieRememberMe(userDetails);
        else jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        String role = String.valueOf(userDetails.getAuthorities());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new JwtResponse(jwtCookie, userDetails.getId(),
                        userDetails.getUsername(), userDetails.getEmail(), role));
    }
}
